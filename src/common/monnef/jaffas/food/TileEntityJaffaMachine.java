package monnef.jaffas.food;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import net.minecraft.src.*;

public abstract class TileEntityJaffaMachine extends TileEntity implements IPowerReceptor {
    public static final int fuelSlot = 20;
    public int burnTime;
    public int burnItemTime;
    protected IPowerProvider powerProvider;
    private int powerNeeded;

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
        if (mod_jaffas.debug) System.out.println("power provider set");
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

}
