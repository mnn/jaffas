/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import monnef.core.utils.TileEntityHelper;
import monnef.jaffas.power.api.IKitchenUnitAppliance;
import monnef.jaffas.power.block.common.TileEntityMachine;
import monnef.jaffas.power.common.BuildCraftHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityKitchenUnit extends TileEntityMachine {
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
                IPowerProvider provider = teReceptor.getPowerProvider();
                if (BuildCraftHelper.gotFreeSpaceInEnergyStorage(provider) && BuildCraftHelper.doesWantEnergy(teReceptor, ForgeDirection.DOWN)) {
                    float extracted = powerProvider.useEnergy(5, powerNeeded, true);
                    provider.receiveEnergy(extracted, ForgeDirection.DOWN);
                } else {
                    skipCounter = 20;
                }
            }
        }
    }
}
