/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityGenerator extends TileEntityMachineWithInventory {
    private static final String BURN_TIME_TAG_NAME = "burnTime";
    private static final String BURN_ITEM_TIME_TAG_NAME = "burnItemTime";
    private static final float ENERGY_PER_TICK = 1.05f;
    public int burnTime = 0;
    public int burnItemTime = 1;
    private int SLOT_FUEL = 0;

    private static final int tickEach = 20;
    private int tickCounter = 0;
    private boolean isSwitchgrass;

    private GeneratorState state = GeneratorState.IDLE;

    @Override
    public void doWork() {
    }

    @Override
    public int powerRequest(ForgeDirection from) {
        return 0;
    }

    private enum GeneratorState {
        IDLE, BURNING
    }

    public TileEntityGenerator() {
        super();
    }

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        powerNeeded = 0;
    }

    @Override
    public String getMachineTitle() {
        return "Generator";
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
//                    manager.storeEnergy(tickEach);
                    IIntegerCoordinates pos = (new IntegerCoordinates(this)).shiftInDirectionBy(ForgeDirection.UP, 1);
                    TileEntity consumerTile = worldObj.getBlockTileEntity(pos.getX(), pos.getY(), pos.getZ());
                    if (isPowerTile(consumerTile)) {
                        float energy = tickEach * ENERGY_PER_TICK * getSwitchgrassCoef();
                        ((IPowerReceptor) consumerTile).getPowerProvider().receiveEnergy(energy, ForgeDirection.DOWN);
                    }
                }

                /*&& manager.getFreeSpaceInBuffer() > 0*/
                if (burnTime <= 0 && gotSpaceInEnergyBuffer()) {
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

//        manager.tick();
    }

    private boolean isPowerTile(TileEntity tile) {
        if (tile instanceof IPowerReceptor) {
            IPowerProvider receptor = ((IPowerReceptor) tile).getPowerProvider();

            return receptor != null;
        }

        return false;
    }

    private boolean gotSpaceInEnergyBuffer() {
        // TODO: rewrite
        return powerProvider.getEnergyStored() < powerProvider.getMaxEnergyStored();
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
                isSwitchgrass = fuelStack.itemID == JaffasFood.blockSwitchgrass.blockID || fuelStack.itemID == JaffasFood.blockSwitchgrassSolid.blockID;
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
}
