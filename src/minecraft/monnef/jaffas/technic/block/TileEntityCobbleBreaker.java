/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import cpw.mods.fml.client.FMLClientHandler;
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
import net.minecraftforge.common.ForgeDirection;

import java.util.HashSet;

import static monnef.jaffas.technic.block.ContainerCompost.SLOT_INPUT;
import static monnef.jaffas.technic.block.ContainerCompost.SLOT_OUTPUT;

public class TileEntityCobbleBreaker extends TileEntity implements IInventory, ISidedInventory {
    private static final String TAG_SHOW_EFFECT = "showBreakEffect";
    private ItemStack[] inv;
    private int workCounter = 0;
    private static final int WORK_EVERY_N_TICKS = 20 * 5;
    private static HashSet<Integer> validToolIDs;

    static {
        validToolIDs = new HashSet<Integer>();
        registerTool(Item.pickaxeWood);
        registerTool(Item.pickaxeStone);
        registerTool(Item.pickaxeGold);
        registerTool(Item.pickaxeDiamond);
    }

    private boolean showBreakEffect;
    private IntegerCoordinates cachedFacingBlockCoords;

    /**
     * Register valid tool for cobble mining. Only itemID sensitive.
     *
     * @param item Tool.
     */
    public static void registerTool(Item item) {
        validToolIDs.add(item.itemID);
    }

    public TileEntityCobbleBreaker() {
        inv = new ItemStack[2];
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

        workCounter++;
        if (workCounter >= WORK_EVERY_N_TICKS) {
            workCounter = 0;
            if (isToolValid() && cobblePresent() && spaceInOutputSlot()) {
                mineCobble();
                damageTool();
            } else {
                workCounter = -20 * 10;
            }
        }
    }

    private void createDigParticles() {
        IntegerCoordinates pos = getFacingBlockCachedCoordinates();

        for (int i = 0; i < 5; i++) {
            float speed = 0.33f;
            float mx = RandomHelper.generateRandomFromSymmetricInterval(speed);
            float my = RandomHelper.generateRandomFromSymmetricInterval(speed);
            float mz = RandomHelper.generateRandomFromSymmetricInterval(speed);
            EntityFX fx = new EntityDiggingFX(worldObj, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, mx, my, mz, Block.cobblestone, 0, 0, FMLClientHandler.instance().getClient().renderEngine);
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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList tagList = tagCompound.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < inv.length) {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            ItemStack stack = inv[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
    }

    @Override
    public Packet getDescriptionPacket() {
        Packet132TileEntityData packet = (Packet132TileEntityData) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.customParam1 : new NBTTagCompound();
        writeToNBT(tag);
        tag.setBoolean(TAG_SHOW_EFFECT, showBreakEffect);
        if (showBreakEffect) showBreakEffect = false;
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.customParam1;
        readFromNBT(tag);
        showBreakEffect = tag.getBoolean(TAG_SHOW_EFFECT);
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
        } else {
            return new int[]{SLOT_INPUT};
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (slot == SLOT_INPUT) {
            return side != ForgeDirection.DOWN.ordinal();
        } else if (slot == SLOT_OUTPUT) {
            return false; // don't allow inserintg to the output slot
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if (slot == SLOT_INPUT) {
            return side != ForgeDirection.DOWN.ordinal();
        } else if (slot == SLOT_OUTPUT) {
            return side == ForgeDirection.DOWN.ordinal();
        }

        return false;
    }
}
