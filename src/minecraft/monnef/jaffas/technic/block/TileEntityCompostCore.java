/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.common.MultiBlockHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import static monnef.jaffas.technic.block.ContainerCompost.SLOT_INPUT;
import static monnef.jaffas.technic.block.ContainerCompost.SLOT_OUTPUT;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.AIR;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.CON_ALLOY;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.CON_GLASS;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.ME;

public class TileEntityCompostCore extends TileEntity implements IInventory, ISidedInventory {
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

    public TileEntityCompostCore() {
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

                    worldObj.setBlock(wx, wy, wz, JaffasTechnic.dummyConstructionBlock.blockID);
                    worldObj.markBlockForUpdate(wx, wy, wz);
                    TileEntityConstructionDummy dummyTE = (TileEntityConstructionDummy) worldObj.getBlockTileEntity(wx, wy, wz);
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
                    int blockId = worldObj.getBlockId(wx, wy, wz);

                    if (blockId != JaffasTechnic.dummyConstructionBlock.blockID)
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

        isValid = tagCompound.getBoolean("IsValid");
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

        tagCompound.setBoolean("IsValid", isValid);
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
