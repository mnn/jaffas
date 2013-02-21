package monnef.jaffas.food.block;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import monnef.jaffas.food.Log;
import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public abstract class TileEntityJaffaMachine extends TileEntity implements IPowerReceptor, ISidedInventory {
    public int fuelSlot;
    public int burnTime;
    public int burnItemTime;
    public IPowerProvider powerProvider;
    private int powerNeeded;

    protected ItemStack[] inv;

    public TileEntityJaffaMachine(int powerNeeded) {
        super();
        burnTime = 0;
        if (PowerFramework.currentFramework != null) {
            powerProvider = PowerFramework.currentFramework.createPowerProvider();
            powerProvider.configure(20, 10, 50, powerNeeded, powerNeeded + 1);
            this.powerNeeded = powerNeeded;
        }
    }

    public static boolean isItemFuel(ItemStack par0ItemStack) {
        return TileEntityFurnace.isItemFuel(par0ItemStack);
    }

    protected void tryGetFuel() {
        if (powerProvider != null) {
            if (powerProvider.getEnergyStored() >= powerNeeded) {
                float mj = powerProvider.useEnergy(powerNeeded, powerNeeded, true);
                if (mj > 0) {
                    burnTime = Math.round(mj);
                    burnItemTime = burnTime;
                    return;
                }
            }
        }

        ItemStack item = getStackInSlot(fuelSlot);
        if (item == null) {
            return;
        }

        int fuelBurnTime = TileEntityFurnace.getItemBurnTime(item);
        if (fuelBurnTime > 0) {
            if (item.itemID == Item.bucketLava.shiftedIndex) {
                setInventorySlotContents(fuelSlot, new ItemStack(Item.bucketEmpty));
            } else {
                item.stackSize--;
            }

            if (item.stackSize <= 0 && !worldObj.isRemote) setInventorySlotContents(fuelSlot, null);

            burnItemTime = fuelBurnTime;
            burnTime = fuelBurnTime;
        } else {
            return;
        }
    }

    public abstract ItemStack getStackInSlot(int slot);

    public abstract void setInventorySlotContents(int slot, ItemStack stack);

    public abstract int getInventoryStackLimit();

    public int getBurnTimeRemainingScaled(int par1) {
        if (burnItemTime == 0) {
            burnItemTime = 200;
        }

        return (burnTime * par1) / burnItemTime;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        burnTime = tagCompound.getInteger("burnTime");
        burnItemTime = tagCompound.getInteger("burnItemTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("burnTime", burnTime);
        tagCompound.setInteger("burnItemTime", burnItemTime);
    }


    public void setPowerProvider(IPowerProvider provider) {
        if (mod_jaffas_food.debug) Log.printInfo("power provider set");
        this.powerProvider = provider;
    }

    public IPowerProvider getPowerProvider() {
        return this.powerProvider;
    }

    public void doWork() {
    }

    public int powerRequest() {
        return powerProvider.getMaxEnergyReceived();
    }

    //@return How many items we added
    protected int addItemToInventory(ItemStack stack, boolean doAdd) {
        int free = -1;
        boolean addToStack = false;
        int ret;

        for (int i = 0; i < fuelSlot; i++) {
            if (inv[i] == null) {
                free = i;
                i = fuelSlot;
            } else if (inv[i].itemID == stack.itemID && inv[i].stackSize < inv[i].getMaxStackSize()) {
                addToStack = true;
                free = i;
                i = fuelSlot;
            }
        }

        if (free != -1) {
            if (addToStack) {
                int newStackSize = stack.stackSize + inv[free].stackSize;
                if (doAdd) inv[free].stackSize += stack.stackSize;

                if (newStackSize > stack.getMaxStackSize()) {
                    int overflowItemsCount = newStackSize % stack.getMaxStackSize();
                    if (doAdd) inv[free].stackSize = stack.getMaxStackSize();

                    ItemStack c = stack.copy();
                    c.stackSize = overflowItemsCount;
                    ret = stack.stackSize - overflowItemsCount;
                    ret += addItemToInventory(c, doAdd);
                } else {
                    ret = stack.stackSize;
                }
            } else {
                if (doAdd) inv[free] = stack;
                ret = stack.stackSize;
            }
        } else {
            ret = 0;
        }

        return ret;
    }

    public int getFuelSlot() {
        return fuelSlot;
    }

    public boolean inventoryFull() {
        for (int i = 0; i < fuelSlot; i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (stack == null || stack.stackSize != stack.getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }

    public boolean canAddToInventory(EntityItem item) {
        return this.addItemToInventory(item.func_92014_d(), false) > 0;
    }

    @Override
    public int getStartInventorySide(ForgeDirection side) {
        switch (side) {
            case UP:
            case DOWN:
                return fuelSlot;
            default:
                return 0;
        }
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side) {
        switch (side) {
            case UP:
            case DOWN:
                return 1;
            default:
                return fuelSlot;
        }
    }
}
