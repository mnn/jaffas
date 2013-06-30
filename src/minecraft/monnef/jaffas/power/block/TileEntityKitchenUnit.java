/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
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
    public void doWork() {
        if (skipCounter > 0) {
            skipCounter--;
        } else {
            skipCounter = 0;

            TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
            if (BuildCraftHelper.isPowerTile(te)) {
                IPowerProvider provider = ((IPowerReceptor) te).getPowerProvider();
                if (BuildCraftHelper.gotFreeSpaceInEnergyStorage(provider)) {
                    float extracted = powerProvider.useEnergy(5, powerNeeded, true);
                    provider.receiveEnergy(extracted, ForgeDirection.DOWN);
                } else {
                    skipCounter = 20;
                }
            }
        }
    }
}
