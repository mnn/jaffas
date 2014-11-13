/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.block.TileMachineWithInventory;
import monnef.core.common.ContainerRegistry;
import monnef.core.power.PowerValues;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

@ContainerRegistry.ContainerTag(slotsCount = 1, outputSlotsCount = 0, containerClassName = "monnef.core.block.ContainerMachine", guiClassName = "monnef.jaffas.power.client.GuiContainerGenerator")
public class TileGenerator extends TileMachineWithInventory {
    private static final String BURN_TIME_TAG_NAME = "burnTime";
    private static final String BURN_ITEM_TIME_TAG_NAME = "burnItemTime";
    private static final float ENERGY_PER_TICK = 10.5f * PowerValues.totalPowerGenerationCoef();
    public int burnTime = 0;
    public int burnItemTime = 1;

    public static final int SLOT_FUEL = 0;

    private boolean isSwitchgrass;

    private GeneratorState state = GeneratorState.IDLE;

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
    public String getInventoryName() {
        return "jaffas.power.generator";
    }

    @Override
    public int getIntegersToSyncCount() {
        return super.getIntegersToSyncCount() + 2;
    }

    @Override
    public int getCurrentValueOfIntegerToSync(int index) {
        int ret = super.getCurrentValueOfIntegerToSync(index);
        switch (index - super.getIntegersToSyncCount()) {
            case 0:
                ret = burnTime;
                break;

            case 1:
                ret = burnItemTime;
                break;
        }

        return ret;
    }

    @Override
    public void setCurrentValueOfIntegerToSync(int index, int value) {
        super.setCurrentValueOfIntegerToSync(index, value);
        switch (index - super.getIntegersToSyncCount()) {
            case 0:
                burnTime = value;
                break;

            case 1:
                burnItemTime = value;
                break;
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    public int getEnergyGeneratedThisTick() {
        int value = burnTime > 0 ? Math.round(ENERGY_PER_TICK * getSwitchgrassCoef()) : 0;
        setGeneratedPowerLastTick(value);
        return value;
    }

    @Override
    public ForgeDirection[] getValidCustomerDirections() {
        return CUSTOMER_DIRECTIONS_NOT_BOTTOM;
    }

    @Override
    protected void onAfterPowerSourceHandling() {
        super.onAfterPowerSourceHandling();
        if (burnTime <= 0 && (gotCustomer() || !isInternalEnergyStorageFull()) && !isBeingPoweredByRedstone()) {
            burnTime = 0;
            tryGetFuel();
        }

        GeneratorState newState;
        newState = isBurning() ? GeneratorState.BURNING : GeneratorState.IDLE;
        if (newState != state) {
            sendUpdate();
        }
        state = newState;
    }

    @Override
    protected void onBeforePowerSourceHandling() {
        super.onBeforePowerSourceHandling();
        if (burnTime > 0) burnTime -= 1;
    }

    private float getSwitchgrassCoef() {
        return isSwitchgrass ? 1.4f : 1;
    }

    private void tryGetFuel() {
        ItemStack fuelStack = getStackInSlot(SLOT_FUEL);
        if (fuelStack != null) {
            int fuelBurnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
            if (fuelBurnTime > 0) {
                fuelStack.stackSize--;
                isSwitchgrass = fuelStack.getItem() == Item.getItemFromBlock(ContentHolder.blockSwitchgrass) || fuelStack.getItem() == Item.getItemFromBlock(ContentHolder.blockSwitchgrassSolid);
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
                worldObj.func_147451_t(xCoord, yCoord, zCoord); // updateAllLightTypes
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
