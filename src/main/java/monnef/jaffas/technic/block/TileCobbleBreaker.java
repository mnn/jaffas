/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.common.ContainerRegistry;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.IntegerCoordinates;
import monnef.core.utils.NBTHelper;
import monnef.core.utils.RandomHelper;
import monnef.jaffas.food.item.ItemJaffaTool;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

import static monnef.jaffas.technic.block.ContainerCobbleBreaker.SLOT_FUEL;
import static monnef.jaffas.technic.block.ContainerCobbleBreaker.SLOT_INPUT;
import static monnef.jaffas.technic.block.ContainerCobbleBreaker.SLOT_OUTPUT;

@ContainerRegistry.ContainerTag(slotsCount = 3, containerClassName = "monnef.jaffas.technic.block.ContainerCobbleBreaker", guiClassName = "monnef.jaffas.technic.client.GuiCobbleBreaker")
public class TileCobbleBreaker extends TileEntity implements IInventory, ISidedInventory {
    private static final String SHOW_EFFECT_TAG = "showBreakEffect";
    private static final String BURN_TIME_TAG = "burnTime";
    private static final String BURN_ITEM_TIME_TAG = "burnItemTime";
    private static final String WORK_TIME_TAG = "burnTime";

    private ItemStack[] inv;
    private int workCounter = 0;
    private int burnTime = 0;
    private int burnItemTime = 1;
    private int tickCounter = 0;
    private static int timerInSeconds = 10;
    private static boolean timerSet = false;
    private static final int TICK_QUANTUM = 20;
    private static int WORK_EVERY_N_TICKS = 20 * timerInSeconds;
    private static int BURN_EVERY_N_TICKS = 10;
    private static HashMap<Item, ValidToolRecord> validTools;
    private ContainerRegistry.ContainerDescriptor containerDescriptor;

    static {
        validTools = new HashMap<Item, ValidToolRecord>();
        registerTool(Items.wooden_pickaxe);
        registerTool(Items.stone_pickaxe);
        registerTool(Items.golden_pickaxe);
        registerTool(Items.diamond_pickaxe);

        if (TICK_QUANTUM % BURN_EVERY_N_TICKS != 0) {
            throw new RuntimeException("TICK_QUANTUM must be dividable by BURN_EVERY_N_TICKS!");
        }
    }

    private boolean showBreakEffect;
    private IntegerCoordinates cachedFacingBlockCoords;

    private static class ValidToolRecord {
        private final Item item;
        private final boolean jaffarrolTool;

        private ValidToolRecord(Item item) {
            this(item, false);
        }

        private ValidToolRecord(Item item, boolean jaffarrolTool) {
            this.item = item;
            this.jaffarrolTool = jaffarrolTool;
        }

        private Item getItem() {
            return item;
        }

        private boolean isJaffarrolTool() {
            return jaffarrolTool;
        }
    }

    public static void setTimer(int seconds) {
        if (timerSet) {
            throw new RuntimeException("timer already set");
        }
        timerSet = true;
        timerInSeconds = seconds;
    }

    /**
     * Register valid tool for cobble mining. Only itemID sensitive.
     *
     * @param item Tool.
     */
    public static void registerTool(Item item) {
        registerTool(item, false);
    }

    public static void registerJaffarrolTool(Item item) {
        registerTool(item, true);
    }

    public static void registerTool(Item item, boolean isJaffarrol) {
        ValidToolRecord rec = new ValidToolRecord(item, isJaffarrol);
        validTools.put(item, rec);
    }

    public TileCobbleBreaker() {
        setupContainerDescriptor();
        inv = new ItemStack[getSizeInventory()];
    }

    private void setupContainerDescriptor() {
        containerDescriptor = ContainerRegistry.getContainerPrototype(this.getClass());
    }

    public ContainerRegistry.ContainerDescriptor getContainerDescriptor() {
        return containerDescriptor;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (showBreakEffect) {
            showBreakEffect = false;
            createDigParticles();
        }

        if (worldObj.isRemote) {
            return;
        }

        tickCounter++;
        if (tickCounter % TICK_QUANTUM == 0) {
            if (isBurning() && tickCounter % BURN_EVERY_N_TICKS == 0) {
                burnTime--;
            }
            if (isWorking()) {
                if (canWork()) {
                    if (isBurning()) {
                        workCounter += TICK_QUANTUM;
                        if (workCounter >= WORK_EVERY_N_TICKS) {
                            workCounter = 0;
                            mineCobble();
                            damageToolIfValid();
                        }
                    } else {
                        tryGetFuel();
                        if (!isBurning()) {
                            // cancel current work - no more fuel
                            workCounter = 0;
                            damageToolIfValid();
                        }
                    }
                } else {
                    // cancel work, something has changed
                    workCounter = 0;
                    damageToolIfValid();
                }
            } else {
                boolean canWorkNow = canWork();
                if (!isBurning() && canWorkNow) {
                    tryGetFuel();
                }
                if (isBurning() && canWorkNow) {
                    // start working
                    workCounter = 1;
                }
            }
        }
    }

    private void damageToolIfValid() {
        if (isToolValid()) {
            damageTool();
        }
    }

    private boolean isWorking() {
        return workCounter > 0;
    }

    private boolean canWork() {
        return isToolValid() && cobblePresent() && spaceInOutputSlot();
    }

    private void tryGetFuel() {
        ItemStack fuelStack = inv[SLOT_FUEL];
        if (fuelStack != null) {
            int fuelBurnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
            if (fuelBurnTime > 0) {
                fuelStack.stackSize--;
                if (fuelStack.stackSize <= 0) {
                    setInventorySlotContents(SLOT_FUEL, null);
                }

                burnTime = fuelBurnTime;
                burnItemTime = fuelBurnTime;
            }
        }
    }

    private boolean isBurning() {
        return burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    private void createDigParticles() {
        IntegerCoordinates pos = getFacingBlockCachedCoordinates();
        int partCount = RandomHelper.generateRandomFromInterval(4, 10);
        for (int i = 0; i < partCount; i++) {
            float speed = 0.33f;
            float mx = RandomHelper.generateRandomFromSymmetricInterval(speed);
            float my = RandomHelper.generateRandomFromSymmetricInterval(speed);
            float mz = RandomHelper.generateRandomFromSymmetricInterval(speed);
            float radius = 0.4f;
            float sx = RandomHelper.generateRandomFromSymmetricInterval(radius);
            float sy = RandomHelper.generateRandomFromSymmetricInterval(radius);
            float sz = RandomHelper.generateRandomFromSymmetricInterval(radius);
            EntityFX fx = new EntityDiggingFX(worldObj, pos.getX() + .5 + sx, pos.getY() + .5 + sy, pos.getZ() + .5 + sz, mx, my, mz, Blocks.cobblestone, 0, 0);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }

    public void forceUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void damageTool() {
        ItemStack stack = inv[SLOT_INPUT];
        int curr = stack.getItemDamage();
        int newDmg = curr + 1;
        if (newDmg > stack.getMaxDamage()) inv[SLOT_INPUT] = null;
        else stack.setItemDamage(newDmg);
    }

    private void mineCobble() {
        IntegerCoordinates pos = getFacingBlockCachedCoordinates();
        BlockHelper.setAir(worldObj, pos.getX(), pos.getY(), pos.getZ());
        showBreakEffect = true;
        forceUpdate();
        ItemStack stack = inv[SLOT_OUTPUT];
        if (stack == null) inv[SLOT_OUTPUT] = new ItemStack(Blocks.cobblestone);
        else inv[SLOT_OUTPUT].stackSize++;
    }

    private IntegerCoordinates getFacingBlockCachedCoordinates() {
        if (cachedFacingBlockCoords == null) {
            cachedFacingBlockCoords = JaffasTechnic.cobbleBreaker.calculateFacingBlock(worldObj, xCoord, yCoord, zCoord);
        }
        return cachedFacingBlockCoords;
    }

    private boolean spaceInOutputSlot() {
        ItemStack stack = inv[SLOT_OUTPUT];
        if (stack == null) return true;
        if (stack.getItem() != Item.getItemFromBlock(Blocks.cobblestone)) return false;
        return stack.stackSize < stack.getMaxStackSize();
    }

    private boolean cobblePresent() {
        IntegerCoordinates pos = getFacingBlockCachedCoordinates();
        Block b = worldObj.getBlock(pos.getX(), pos.getY(), pos.getZ());
        return b == Blocks.cobblestone;
    }

    private boolean isToolValid() {
        ItemStack stack = inv[SLOT_INPUT];
        if (stack == null) return false;
        ValidToolRecord rec = validTools.get(stack.getItem());
        if (rec == null) return false;
        if (!rec.isJaffarrolTool()) return true;
        return !ItemJaffaTool.nearlyDestroyed(stack);
    }

    @Override
    public int getSizeInventory() {
        return getContainerDescriptor().getSlotsCount();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inv[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Inventory", NBTHelper.TagTypes.TAG_Compound);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound innerTag = (NBTTagCompound) tagList.getCompoundTagAt(i);
            byte slot = innerTag.getByte("Slot");
            if (slot >= 0 && slot < inv.length) {
                inv[slot] = ItemStack.loadItemStackFromNBT(innerTag);
            }
        }

        setBurnTime(tag.getInteger(BURN_TIME_TAG));
        setBurnItemTime(tag.getInteger(BURN_ITEM_TIME_TAG));
        setWorkCounter(tag.getInteger(WORK_TIME_TAG));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            ItemStack stack = inv[i];
            if (stack != null) {
                NBTTagCompound innterTag = new NBTTagCompound();
                innterTag.setByte("Slot", (byte) i);
                stack.writeToNBT(innterTag);
                itemList.appendTag(innterTag);
            }
        }
        tag.setTag("Inventory", itemList);
        tag.setInteger(BURN_TIME_TAG, getBurnTime());
        tag.setInteger(BURN_ITEM_TIME_TAG, getBurnItemTime());
        tag.setInteger(WORK_TIME_TAG, getWorkMeter());
    }

    @Override
    public Packet getDescriptionPacket() {
        S35PacketUpdateTileEntity packet = (S35PacketUpdateTileEntity) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.func_148857_g() : new NBTTagCompound();
        writeToNBT(tag);
        tag.setBoolean(SHOW_EFFECT_TAG, showBreakEffect);
        if (showBreakEffect) showBreakEffect = false;
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.func_148857_g();
        readFromNBT(tag);
        showBreakEffect = tag.getBoolean(SHOW_EFFECT_TAG);
    }

    @Override
    public String getInventoryName() {
        return "jaffas.cobbleBreaker";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == ForgeDirection.DOWN.ordinal()) {
            return new int[]{SLOT_OUTPUT};
        } else if (side == ForgeDirection.UP.ordinal()) {
            return new int[]{SLOT_INPUT};
        } else {
            return new int[]{SLOT_FUEL};
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (slot == SLOT_INPUT) {
            return side == ForgeDirection.UP.ordinal();
        } else if (slot == SLOT_FUEL) {
            return side != ForgeDirection.UP.ordinal() && side != ForgeDirection.DOWN.ordinal();
        } else if (slot == SLOT_OUTPUT) {
            return false; // don't allow inserintg to the output slot
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if (slot == SLOT_INPUT) {
            return side == ForgeDirection.UP.ordinal();
        } else if (slot == SLOT_OUTPUT) {
            return side == ForgeDirection.DOWN.ordinal();
        } else if (slot == SLOT_FUEL) {
            return side != ForgeDirection.UP.ordinal() && side != ForgeDirection.DOWN.ordinal();
        }

        return false;
    }

    public int getWorkMeter() {
        return workCounter;
    }

    public void setWorkCounter(int workCounter) {
        this.workCounter = workCounter;
    }

    public int getMaxWorkMeter() {
        return WORK_EVERY_N_TICKS;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getBurnItemTime() {
        return burnItemTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public void setBurnItemTime(int burnItemTime) {
        this.burnItemTime = burnItemTime;
    }
}
