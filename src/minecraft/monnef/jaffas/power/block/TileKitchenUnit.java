/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import monnef.core.utils.TileEntityHelper;
import monnef.jaffas.power.api.IKitchenUnitAppliance;
import monnef.jaffas.food.block.common.TileEntityMachine;
import monnef.jaffas.power.common.BuildCraftHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileKitchenUnit extends TileEntityMachine {
    public static final ForgeDirection INPUT_SIDE_OF_APPLIANCE = ForgeDirection.DOWN;
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

            TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
            if (te != null && te instanceof IKitchenUnitAppliance) {
                if (!BuildCraftHelper.isPowerTile(te)) {
                    throw new RuntimeException("is KUAppliance but doesn't accept power? my pos: " + TileEntityHelper.getFormattedCoordinates(this));
                }
                IPowerReceptor teReceptor = (IPowerReceptor) te;
                PowerHandler.PowerReceiver appliancePowerReceiver = teReceptor.getPowerReceiver(INPUT_SIDE_OF_APPLIANCE);
                if (BuildCraftHelper.gotFreeSpaceInEnergyStorageAndWantsEnergy(appliancePowerReceiver)) {
                    float extracted = powerHandler.useEnergy(5, powerNeeded, true);
                    appliancePowerReceiver.receiveEnergy(PowerHandler.Type.STORAGE, extracted, INPUT_SIDE_OF_APPLIANCE);
                } else {
                    skipCounter = 20;
                }
            }
        }
    }
}
