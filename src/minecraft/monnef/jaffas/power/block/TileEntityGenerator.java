package monnef.jaffas.power.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import monnef.jaffas.power.common.PowerProviderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityGenerator extends TileEntityMachineWithInventory implements IPowerProvider {
    private final PowerProviderManager manager;
    private static final String BURN_TIME_TAG_NAME = "burnTime";
    private static final String BURN_ITEM_TIME_TAG_NAME = "burnItemTime";
    public int burnTime = 0;
    public int burnItemTime = 1;
    private int SLOT_FUEL = 0;

    private static final int tickEach = 20;
    private int tickCounter = 0;

    private GeneratorState state = GeneratorState.IDLE;

    private enum GeneratorState {
        IDLE, BURNING
    }

    public TileEntityGenerator() {
        super();

        manager = new PowerProviderManager();
    }

    @Override
    public IPowerProviderManager getPowerProviderManager() {
        return manager;
    }

    @Override
    public String getMachineTitle() {
        return "Generator";
    }

    @Override
    protected void onTick(int number) {
        super.onTick(number);
        if (number == 1) {
            boolean[] sidesMask = new boolean[6];
            sidesMask[ForgeDirection.UP.ordinal()] = true;
            manager.initialize(20, 500, this, false, sidesMask);
        }
    }

    public int getSizeInventory() {
        return 1;
    }

    @Override
    public String getInvName() {
        return "jaffas.power.generator";
    }

    @Override
    public int getIntegersToSyncCount() {
        return 2;
    }

    @Override
    public int getCurrentValueOfIntegerToSync(int index) {
        switch (index) {
            case 0:
                return burnTime;

            case 1:
                return burnItemTime;
        }

        return -1;
    }

    @Override
    public void setCurrentValueOfIntegerToSync(int index, int value) {
        switch (index) {
            case 0:
                burnTime = value;
                break;

            case 1:
                burnItemTime = value;
                break;

            default:
                return;
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        tickCounter++;

        if (tickCounter % tickEach == 0) {
            if (!worldObj.isRemote) {
                GeneratorState newState;

                if (burnTime > 0) {
                    burnTime -= tickEach;
                    manager.storeEnergy(tickEach);
                }

                if (burnTime <= 0 && manager.getFreeSpaceInBuffer() > 0) {
                    burnTime = 0;
                    tryGetFuel();
                }

                newState = isBurning() ? GeneratorState.BURNING : GeneratorState.IDLE;
                if (newState != state) {
                    sendUpdate();
                }
                state = newState;
            }
        }

        manager.tick();
    }

    private void tryGetFuel() {
        ItemStack fuelStack = inventory[SLOT_FUEL];
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

    @SideOnly(Side.CLIENT)
    public int getBurnTimeScaled(int par1) {
        if (burnTime == 0) {
            burnTime = 200;
        }

        return (burnTime * par1) / burnItemTime;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        boolean lastIsBurning = isBurning();
        super.readFromNBT(tag);
        manager.readFromNBT(tag);
        this.burnTime = tag.getInteger(BURN_TIME_TAG_NAME);
        this.burnItemTime = tag.getInteger(BURN_ITEM_TIME_TAG_NAME);

        if (worldObj != null && worldObj.isRemote) {
            if (lastIsBurning != isBurning()) {
                worldObj.updateAllLightTypes(xCoord, yCoord, zCoord);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger(BURN_TIME_TAG_NAME, this.burnTime);
        tag.setInteger(BURN_ITEM_TIME_TAG_NAME, this.burnItemTime);
        manager.writeToNBT(tag);
    }
}
