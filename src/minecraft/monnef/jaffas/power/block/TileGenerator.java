/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.block.common.TileEntityMachineWithInventory;
import monnef.jaffas.power.common.BuildCraftHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;

public class TileGenerator extends TileEntityMachineWithInventory {
    private static final String BURN_TIME_TAG_NAME = "burnTime";
    private static final String BURN_ITEM_TIME_TAG_NAME = "burnItemTime";
    private static final float ENERGY_PER_TICK = 1.05f;
    private static final ForgeDirection[] CUSTOMER_DIRECTIONS = new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST};
    public int burnTime = 0;
    public int burnItemTime = 1;

    public static final int SLOT_FUEL = 0;

    private static final int tickEach = 20;
    private int tickCounter = 0;
    private boolean isSwitchgrass;

    private GeneratorState state = GeneratorState.IDLE;
    private ForgeDirection customerDirection = ForgeDirection.UNKNOWN;
    private int startDirNumber;

    public TileGenerator() {
        setIsRedstoneSensitive();
    }

    @Override
    protected void doMachineWork() {
    }

    private enum GeneratorState {
        IDLE, BURNING
    }

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        configureAsPowerSource();
    }

    @Override
    public String getMachineTitle() {
        return "Generator";
    }

    @Override
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
                onServerTick();
            }
        }
    }

    private void onServerTick() {
        refreshCustomer();

        if (burnTime > 0) {
            burnTime -= tickEach;
            if (gotCustomer()) {
                TileEntity consumerTile = getConsumerTile();
                if (BuildCraftHelper.isPowerTile(consumerTile)) {
                    float energy = tickEach * ENERGY_PER_TICK * getSwitchgrassCoef();
                    //IPowerProvider customerPowerProvider = ((IPowerReceptor) consumerTile).getPowerProvider();
                    //customerPowerProvider.receiveEnergy(energy, customerDirection.getOpposite());
                    PowerHandler.PowerReceiver customersPowerReceiver = ((IPowerReceptor) consumerTile).getPowerReceiver(customerDirection.getOpposite());
                    customersPowerReceiver.receiveEnergy(PowerHandler.Type.ENGINE, energy, customerDirection.getOpposite());
                }
            }
        }

        if (burnTime <= 0 && gotCustomer() && !isBeingPoweredByRedstone()) {
            burnTime = 0;
            tryGetFuel();
        }

        refreshState();
    }

    private TileEntity getConsumerTile() {
        return getConsumerTileInDirection(customerDirection);
    }

    private void refreshState() {
        GeneratorState newState;
        newState = isBurning() ? GeneratorState.BURNING : GeneratorState.IDLE;
        if (newState != state) {
            sendUpdate();
        }
        state = newState;
    }

    private TileEntity getConsumerTileInDirection(ForgeDirection dir) {
        return (new IntegerCoordinates(this)).shiftInDirectionBy(dir, 1).getBlockTileEntity();
    }

    private boolean gotCustomer() {
        return customerDirection != ForgeDirection.UNKNOWN;
    }

    private boolean isCustomerInDirection(ForgeDirection dir) {
        TileEntity customer = getConsumerTileInDirection(dir);
        if (!BuildCraftHelper.isPowerTile(customer)) return false;
        //IPowerProvider customerProvider = ((IPowerReceptor) customer).getPowerProvider();
        //return BuildCraftHelper.gotFreeSpaceInEnergyStorage(customerProvider) && BuildCraftHelper.doesWantEnergyFromDirection((IPowerReceptor) customer, dir.getOpposite());
        return BuildCraftHelper.gotFreeSpaceInEnergyStorageAndWantsEnergy((IPowerReceptor) customer, customerDirection.getOpposite());
    }

    private void refreshCustomer() {
        customerDirection = ForgeDirection.UNKNOWN;
        setNextCustomerDirection();
        int tested = 0;

        while (tested < CUSTOMER_DIRECTIONS.length) {
            ForgeDirection currentDirection = CUSTOMER_DIRECTIONS[startDirNumber];
            if (isCustomerInDirection(currentDirection)) {
                IPowerReceptor consumer = (IPowerReceptor) getConsumerTileInDirection(currentDirection);
                //if (BuildCraftHelper.gotFreeSpaceInEnergyStorage(consumer.getPowerProvider())) {
                if (BuildCraftHelper.gotFreeSpaceInEnergyStorage(consumer, currentDirection.getOpposite())) {
                    customerDirection = currentDirection;
                    return;
                }
            }
            tested++;
            setNextCustomerDirection();
        }

        customerDirection = ForgeDirection.UNKNOWN;
    }

    private void setNextCustomerDirection() {
        startDirNumber++;
        if (startDirNumber >= CUSTOMER_DIRECTIONS.length) startDirNumber = 0;
    }

    private float getSwitchgrassCoef() {
        return isSwitchgrass ? 1.1f : 1;
    }

    private void tryGetFuel() {
        ItemStack fuelStack = inventory[SLOT_FUEL];
        if (fuelStack != null) {
            int fuelBurnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
            if (fuelBurnTime > 0) {
                fuelStack.stackSize--;
                isSwitchgrass = fuelStack.itemID == ContentHolder.blockSwitchgrass.blockID || fuelStack.itemID == ContentHolder.blockSwitchgrassSolid.blockID;
                if (fuelStack.stackSize <= 0) {
                    setInventorySlotContents(SLOT_FUEL, null);
                }

                burnTime = fuelBurnTime + 1;
                burnItemTime = burnTime;
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
    }

    @Override
    public boolean isPowerBarRenderingEnabled() {
        return false;
    }
}
