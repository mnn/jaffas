/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.client.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

import static monnef.jaffas.technic.JaffasTechnic.fermenter;
import static monnef.jaffas.technic.block.ContainerFermenter.SLOT_INPUT;
import static monnef.jaffas.technic.block.ContainerFermenter.SLOT_KEG;
import static monnef.jaffas.technic.block.ContainerFermenter.SLOT_OUTPUT;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.NOTHING;

public class TileEntityFermenter extends TileEntity implements IInventory, ISidedInventory {
    private static final String WORK_TIME_TAG = "workCounter";
    private static final String LIQUID_AMOUNT_TAG = "liquidAmount";
    private static final String LIQUID_TYPE_TAG = "liquidType";
    private static final int FERMENTER_CAPACITY = 2;
    private boolean integrityCheckScheduled = true;
    private ItemStack[] inv;
    private int workCounter = 0;
    private int tickCounter = 0;
    private FermentedLiquid liquid = NOTHING;
    private int liquidAmount;

    public enum FermentedLiquid {
        NOTHING,
        BEER_RAW(10),
        WINE_RAW(15),
        BEER,
        WINE;

        // ticks
        private int fermentationLength;

        private FermentedLiquid(int fermentationLengthInMinutes) {
            this.fermentationLength = fermentationLengthInMinutes * 20 * 60;
        }

        FermentedLiquid() {
        }

        public int getFermentationLength() {
            return fermentationLength;
        }

        public boolean canBeFermented() {
            return fermentationLength > 0;
        }
    }

    public TileEntityFermenter() {
        inv = new ItemStack[3];
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
    }

    public void planIntegrityCheck() {
        if (worldObj.isRemote) return;
        integrityCheckScheduled = true;
    }

    @Override
    public void updateEntity() {
        // 2x bucket of brewed hops -> 1x keg

        checkBlockIntegrity();
    }

    private void checkBlockIntegrity() {
        if (worldObj.isRemote) return;
        if (integrityCheckScheduled) {
            integrityCheckScheduled = false;
            boolean failure = false;
            boolean removeBlock = false;

            int bId = worldObj.getBlockId(xCoord, yCoord + 1, zCoord);
            if (bId != getBlockType().blockID) {
                failure = true;
            } else {
                int meta = worldObj.getBlockMetadata(xCoord, yCoord + 1, zCoord);
                if (!fermenter.isSlave(meta)) {
                    failure = true;
                    removeBlock = true;
                }
            }

            if (failure) {
                if (removeBlock) {
                    BlockHelper.setBlock(worldObj, xCoord, yCoord + 1, zCoord, 0);
                }
                int myMeta = getBlockMetadata();
                BlockHelper.setBlock(worldObj, xCoord, yCoord, zCoord, 0);
                invalidate();
                fermenter.dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, myMeta, 0); // last param is fortune
            }
        }
    }

    public boolean playerActivatedBox(EntityPlayer player) {
        player.openGui(JaffasFood.instance, GuiHandler.GuiTypes.COBBLE_BREAKER.ordinal(), worldObj, xCoord, yCoord, zCoord);
        return true;
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

        setWorkCounter(tag.getInteger(WORK_TIME_TAG));
        setLiquid(tag.getInteger(LIQUID_TYPE_TAG));
        setLiquidAmount(tag.getInteger(LIQUID_AMOUNT_TAG));
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
        tag.setInteger(WORK_TIME_TAG, getWorkMeter());
    }

    /*
    @Override
    public Packet getDescriptionPacket() {
        Packet132TileEntityData packet = (Packet132TileEntityData) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.customParam1 : new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.customParam1;
        readFromNBT(tag);
    }
    */

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
            return new int[]{SLOT_KEG};
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (slot == SLOT_INPUT) {
            return side == ForgeDirection.UP.ordinal();
        } else if (slot == SLOT_KEG) {
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
        } else if (slot == SLOT_KEG) {
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
        return liquid.getFermentationLength();
    }

    public FermentedLiquid getLiquid() {
        return liquid;
    }

    public void setLiquid(FermentedLiquid liquid) {
        this.liquid = liquid;
    }

    public void setLiquid(int value) {
        liquid = TileEntityFermenter.FermentedLiquid.values()[value];
    }

    public int getLiquidAmount() {
        return liquidAmount;
    }

    public void setLiquidAmount(int liquidAmount) {
        this.liquidAmount = liquidAmount;
    }

    public boolean canWork() {
        if (!isFull()) return false;
        return liquid.canBeFermented();
    }

    private boolean isFull() {
        refreshLiquidAmount();
        if (liquid == NOTHING) return false;
        return liquidAmount == FERMENTER_CAPACITY;
    }

    private boolean isEmpty() {
        refreshLiquidAmount();
        return liquid == NOTHING;
    }

    public void refreshLiquidAmount() {
        if (liquid == NOTHING) {
            liquidAmount = 0;
        } else if (liquidAmount <= 0) {
            liquidAmount = 0;
            liquid = NOTHING;
        }
    }

    public boolean isWorking() {
        return workCounter > 0;
    }
}
