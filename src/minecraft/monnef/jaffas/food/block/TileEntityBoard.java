package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.utils.ItemHelper;
import monnef.jaffas.food.crafting.RecipesBoard;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import java.util.Random;

import static monnef.jaffas.food.jaffasFood.blockBoard;
import static monnef.jaffas.food.jaffasFood.getItem;

public class TileEntityBoard extends TileEntity implements IInventory, ISidedInventory {
    private ItemStack[] inv;
    public int chopTime;
    private int chopSpeed = startingChopSpeed;
    private static final int startingChopSpeed = 5;
    private static final int maximalChopSpeed = 100;

    public final static int slotInput = 0;
    public final static int slotOutput = 1;
    public final static int slotKnife = 2;

    private static Random rand = new Random();

    private boolean knifeInitialized = false;
    private boolean knifePresent = false;

    public TileEntityBoard() {
        inv = new ItemStack[3];
    }

    @Override
    public void updateEntity() {
        if (!knifeInitialized) {
            knifePresent = blockBoard.hasKnife(getBlockMetadata());
            knifeInitialized = true;
        }

        if (!worldObj.isRemote) {

            boolean updateInventory = false;
            if (this.knifePresent() && this.canSmelt()) {
                chopTime += chopSpeed;

                if (chopTime >= 200) {
                    chopTime = 0;
                    chopItem();
                    breakKnife();
                    checkKnife();
                    updateInventory = true;
                    if (chopSpeed <= maximalChopSpeed) chopSpeed = chopSpeed + chopSpeed / 5;
                }
            } else {
                this.chopTime = 0;
                chopSpeed = startingChopSpeed;
            }

            if (updateInventory) {
                onInventoryChanged();
            }
        }
    }

    private void breakKnife() {
        //if (rand.nextFloat() < 0.25) {
        ItemStack knife = getStackInSlot(slotKnife);
        if (ItemHelper.DamageItem(knife, 1)) {
            setInventorySlotContents(slotKnife, null);
        }
        //}
    }

    private void chopItem() {
        ItemStack itemInInputSlot = getStackInSlot(slotInput);
        ItemStack recipeOutput = RecipesBoard.getRecipeOutputAndDecreaseInputStack(itemInInputSlot);

        ItemStack itemInOutputSlot = getStackInSlot(slotOutput);

        if (itemInOutputSlot == null) {
            setInventorySlotContents(slotOutput, recipeOutput);
        } else {
            itemInOutputSlot.stackSize += recipeOutput.stackSize;
        }

        if (itemInInputSlot.stackSize <= 0) {
            setInventorySlotContents(slotInput, null);
        }

        onInventoryChanged();
    }

    private boolean canSmelt() {
        ItemStack itemInInputSlot = getStackInSlot(slotInput);
        ItemStack recipeOutput = RecipesBoard.getRecipeOutput(itemInInputSlot);

        // recipe not found
        if (recipeOutput == null) return false;

        ItemStack itemInOutputSlot = getStackInSlot(slotOutput);
        // nothing in output slot => ok
        if (itemInOutputSlot == null) return true;

        // in output slot isn't item from recipe
        if (itemInOutputSlot.itemID != recipeOutput.itemID) return false;

        // overflow in output slot
        if (itemInOutputSlot.getMaxStackSize() < recipeOutput.stackSize + itemInOutputSlot.stackSize) return false;

        return true;
    }

    private boolean knifePresent() {
        ItemStack item = getStackInSlot(slotKnife);
        if (item == null) {
            return false;
        }
        if (item.itemID != getItem(JaffaItem.knifeKitchen).itemID) {
            return false;
        }

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
    public String getInvName() {
        return "jaffas.board";
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    @SideOnly(Side.CLIENT)
    public int getChopTimeScaled(int par1) {
        return this.chopTime * par1 / 200;
    }

    public void checkKnife() {
        boolean isKnifePresent = false;
        ItemStack slotItem = getStackInSlot(slotKnife);
        if (slotItem != null && slotItem.itemID == getItem(JaffaItem.knifeKitchen).itemID) {
            isKnifePresent = true;
        }

        if (isKnifePresent != this.knifePresent) {
            this.knifePresent = isKnifePresent;
            blockBoard.setKnife(knifePresent, worldObj, xCoord, yCoord, zCoord);
        }
    }

    @Override
    public int getStartInventorySide(ForgeDirection side) {
        switch (side) {
            case UP:
                return slotInput;

            case DOWN:
                return slotKnife;

            default:
                return slotOutput;
        }
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side) {
        return 1;
    }
}
