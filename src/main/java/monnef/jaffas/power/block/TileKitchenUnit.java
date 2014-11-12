/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import cofh.api.energy.IEnergyReceiver;
import monnef.core.block.TileMachine;
import monnef.core.utils.TileEntityHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.api.IKitchenUnitAppliance;
import monnef.jaffas.power.common.RedstoneFluxHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileKitchenUnit extends TileMachine {
    public static final ForgeDirection INPUT_SIDE_OF_APPLIANCE = ForgeDirection.DOWN;
    public static final int TRANSFER_RATE = 5;

    private int skipCounter;

    @Override
    public String getMachineTitle() {
        return "kitchenUnit";
    }

    @Override
    protected void doMachineWork() {
        if (skipCounter > 0) {
            skipCounter--;
        } else {
            skipCounter = 0;

            TileEntity te = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            if (te != null && te instanceof IKitchenUnitAppliance) {
                if (!RedstoneFluxHelper.isTilePowerReceiver(te)) {
                    throw new RuntimeException("is KUAppliance but doesn't accept power? my pos: " + TileEntityHelper.getFormattedCoordinates(this));
                }

                IEnergyReceiver applianceEnergyHandler = (IEnergyReceiver) te;
                if (RedstoneFluxHelper.gotFreeSpaceInEnergyStorageAndWantsEnergy(applianceEnergyHandler, INPUT_SIDE_OF_APPLIANCE)) {
                    int extractedSimulated = energyStorage.extractEnergy(TRANSFER_RATE, true);
                    int accepted = applianceEnergyHandler.receiveEnergy(INPUT_SIDE_OF_APPLIANCE, extractedSimulated, false);
                    int extracted = energyStorage.extractEnergy(TRANSFER_RATE, false);
                    if (accepted != extracted) {
                        JaffasFood.Log.printWarning(String.format("Kitchen unit: numbers don't add up - accepted=%d vs. extracted=%d", accepted, extracted));
                    }
                } else {
                    skipCounter = 20;
                }
            }
        }
    }
}
