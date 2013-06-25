/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.IntegerCoordinates;
import monnef.core.utils.RandomHelper;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashSet;

import static monnef.jaffas.technic.block.ContainerCobbleBreaker.SLOT_FUEL;
import static monnef.jaffas.technic.block.ContainerCobbleBreaker.SLOT_INPUT;
import static monnef.jaffas.technic.block.ContainerCobbleBreaker.SLOT_OUTPUT;

public class TileEntityCobbleBreaker extends TileEntity implements IInventory, ISidedInventory {
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
    private static HashSet<Integer> validToolIDs;

    static {
        validToolIDs = new HashSet<Integer>();
        registerTool(Item.pickaxeWood);
        registerTool(Item.pickaxeStone);
        registerTool(Item.pickaxeGold);
        registerTool(Item.pickaxeDiamond);

        if (TICK_QUANTUM % BURN_EVERY_N_TICKS != 0) {
            throw new RuntimeException("TICK_QUANTUM must be dividable by BURN_EVERY_N_TICKS!");
        }
    }

    private boolean showBreakEffect;
    private IntegerCoordinates cachedFacingBlockCoords;

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
        validToolIDs.add(item.itemID);
    }

    public TileEntityCobbleBreaker() {
        inv = new ItemStack[3];
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
            EntityFX fx = new EntityDiggingFX(worldObj, pos.getX() + .5 + sx, pos.getY() + .5 + sy, pos.getZ() + .5 + sz, mx, my, mz, Block.cobblestone, 0, 0, FMLClientHandler.instance().getClient().renderEngine);
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
        BlockHelper.setBlock(worldObj, pos.getX(), pos.getY(), pos.getZ(), 0);
        showBreakEffect = true;
        forceUpdate();
        ItemStack stack = inv[SLOT_OUTPUT];
        if (stack == null) inv[SLOT_OUTPUT] = new ItemStack(Block.cobblestone);
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
        if (stack.itemID != Block.cobblestone.blockID) return false;
        return stack.stackSize < stack.getMaxStackSize();
    }

    private boolean cobblePresent() {
        IntegerCoordinates pos = getFacingBlockCachedCoordinates();
        int bId = worldObj.getBlockId(pos.getX(), pos.getY(), pos.getZ());
        return bId == Block.cobblestone.blockID;
    }

    private boolean isToolValid() {
        ItemStack stack = inv[SLOT_INPUT];
        if (stack == null) return false;
        return validToolIDs.contains(stack.itemID);
    }

    @Override
    public int getSizeInventory() {
        return inv.length;
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
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound innerTag = (NBTTagCompound) tagList.tagAt(i);
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
        Packet132TileEntityData packet = (Packet132TileEntityData) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.customParam1 : new NBTTagCompound();
        writeToNBT(tag);
        tag.setBoolean(SHOW_EFFECT_TAG, showBreakEffect);
        if (showBreakEffect) showBreakEffect = false;
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.customParam1;
        readFromNBT(tag);
        showBreakEffect = tag.getBoolean(SHOW_EFFECT_TAG);
    }

    @Override
    public String getInvName() {
        return "jaffas.board";
    }

    @Override
    public boolean isInvNameLocalized() {
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
