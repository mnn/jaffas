/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.common.ContainerRegistry;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.NBTHelper;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.common.CompostRegister;
import monnef.jaffas.technic.common.MultiBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import static monnef.jaffas.technic.JaffasTechnic.compost;
import static monnef.jaffas.technic.block.ContainerCompost.SLOT_INPUT;
import static monnef.jaffas.technic.block.ContainerCompost.SLOT_OUTPUT;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.AIR;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.CON_ALLOY;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.CON_GLASS;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.ME;

@ContainerRegistry.ContainerTag(slotsCount = 2, containerClassName = "monnef.jaffas.technic.block.ContainerCompost", guiClassName = "monnef.jaffas.technic.client.GuiCompost")
public class TileCompostCore extends TileEntity implements IInventory, ISidedInventory {
    public static final int NUMBER_OF_COMPOST_PRODUCED = 32;
    public static final String TAG_WORK_METER = "workMeter";
    public static final String TAG_TANK_METER = "tankMeter";
    private ItemStack[] inv;

    private static final TemplateMark[][] level0 = new TemplateMark[][]{
            new TemplateMark[]{CON_ALLOY, CON_ALLOY, CON_ALLOY},
            new TemplateMark[]{CON_ALLOY, ME, CON_ALLOY},
            new TemplateMark[]{CON_ALLOY, CON_ALLOY, CON_ALLOY}
    };
    private static final TemplateMark[][] level1 = new TemplateMark[][]{
            new TemplateMark[]{CON_ALLOY, CON_GLASS, CON_ALLOY},
            new TemplateMark[]{CON_GLASS, AIR, CON_GLASS},
            new TemplateMark[]{CON_ALLOY, CON_GLASS, CON_ALLOY}
    };
    private static final TemplateMark[][][] template = new TemplateMark[][][]{level0, level1};

    private boolean isValid;

    private int tankMeter;
    private int workMeter;

    private int sleepCounter;

    public TileCompostCore() {
        inv = new ItemStack[2];
    }

    public boolean getIsValidMultiblock() {
        return isValid;
    }

    public void invalidateMultiblock() {
        isValid = false;
        revertDummies();
    }

    public boolean checkIfProperlyFormed() {
        return MultiBlockHelper.templateFits(worldObj, xCoord, yCoord, zCoord, template);
    }

    public void convertDummies() {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y <= 1; y++) {
                    if (x == 0 && z == 0 && y == 0) // me
                    {
                        BlockHelper.setBlockMetadata(worldObj, xCoord, yCoord, zCoord, 1);
                        continue;
                    }

                    int wx = x + xCoord;
                    int wy = y + yCoord;
                    int wz = z + zCoord;

                    worldObj.setBlock(wx, wy, wz, JaffasTechnic.dummyConstructionBlock);
                    worldObj.markBlockForUpdate(wx, wy, wz);
                    TileConstructionDummy dummyTE = (TileConstructionDummy) worldObj.getTileEntity(wx, wy, wz);
                    dummyTE.setCore(this);
                }
            }
        }

        isValid = true;
    }

    private void revertDummies() {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y <= 1; y++) {
                    if (x == 0 && z == 0 && y == 0) // me
                    {
                        BlockHelper.setBlockMetadata(worldObj, xCoord, yCoord, zCoord, 0);
                        continue;
                    }

                    int wx = x + xCoord;
                    int wy = y + yCoord;
                    int wz = z + zCoord;
                    Block blockCurrent = worldObj.getBlock(wx, wy, wz);

                    if (blockCurrent != JaffasTechnic.dummyConstructionBlock)
                        continue;

                    MultiBlockHelper.setBlockByMark(template[y][z + 1][x + 1], worldObj, wx, wy, wz);
                    worldObj.markBlockForUpdate(wx, wy, wz);
                }
            }
        }
        isValid = false;
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
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public void openInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList tagList = tagCompound.getTagList("Inventory", NBTHelper.TagTypes.TAG_Compound);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < inv.length) {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        readFromNBTIsValid(tagCompound);
        workMeter = tagCompound.getInteger(TAG_WORK_METER);
        tankMeter = tagCompound.getInteger(TAG_TANK_METER);
    }

    public void readFromNBTIsValid(NBTTagCompound tag) {
        isValid = tag.getBoolean("IsValid");
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

        writeToNBTIsValid(tagCompound);
        tagCompound.setInteger(TAG_WORK_METER, workMeter);
        tagCompound.setInteger(TAG_TANK_METER, tankMeter);
    }

    public void writeToNBTIsValid(NBTTagCompound tag) {
        tag.setBoolean("IsValid", isValid);
    }

    @Override
    public String getInventoryName() {
        return "jaffas.board";
    }

    @Override
    public boolean hasCustomInventoryName() {
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

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord - 2, yCoord, zCoord - 2, xCoord + 3, yCoord + 3, zCoord + 3);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBTIsValid(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.func_148857_g();
        readFromNBTIsValid(tag);
    }

    public int getTankMeter() {
        return tankMeter;
    }

    public void setTankMeter(int tankMeter) {
        this.tankMeter = tankMeter;
    }

    public int getMaxTankValue() {
        return 6400;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj.isRemote) return;

        if (sleepCounter++ < 20) {
            return;
        }
        sleepCounter = 0;

        if (isWorking()) {
            workMeter++;
            if (workMeter >= getMaxWork()) {
                ItemStack outputStack = inv[SLOT_OUTPUT];
                boolean compostCreated = true;
                if (outputStack == null) {
                    setInventorySlotContents(SLOT_OUTPUT, new ItemStack(compost, NUMBER_OF_COMPOST_PRODUCED));
                } else if (canProduceCompost()) {
                    outputStack.stackSize += NUMBER_OF_COMPOST_PRODUCED;
                } else {
                    compostCreated = false;
                }

                if (compostCreated) {
                    workMeter = 0;
                    tankMeter = 0;
                } else {
                    workMeter -= 15;
                }
            }
        } else {
            if (canWork()) {
                // start working
                workMeter = 1;
            } else {
                // try pull only when not full
                ItemStack inputStack = inv[SLOT_INPUT];
                if (tankMeter < getMaxTankValue() && inputStack != null) {
                    int value = CompostRegister.getCompostValue(inputStack);
                    if (value != 0) {
                        tankMeter += value;
                        if (tankMeter > getMaxTankValue()) tankMeter = getMaxTankValue();
                        inputStack.stackSize--;
                        if (inputStack.stackSize <= 0) {
                            setInventorySlotContents(SLOT_INPUT, null);
                        }
                    }
                }
            }
        }
    }

    private boolean canWork() {
        if (tankMeter >= getMaxTankValue() && canProduceCompost())
            return true;
        return false;
    }

    public boolean canProduceCompost() {
        ItemStack outputStack = inv[SLOT_OUTPUT];
        if (outputStack == null) return true;
        return outputStack.getItem() == compost && outputStack.stackSize + NUMBER_OF_COMPOST_PRODUCED <= outputStack.getMaxStackSize();
    }

    public int getMaxWork() {
        if (MonnefCorePlugin.debugEnv) return 10;
        return 5 * 60;
    }

    public boolean isWorking() {
        return workMeter > 0;
    }

    public int getWorkMeter() {
        return workMeter;
    }

    public void setWorkMeter(int workMeter) {
        this.workMeter = workMeter;
    }
}
