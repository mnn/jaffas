/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import monnef.core.api.IIntegerCoordinates;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class BuildCraftHelper {
    public static boolean gotFreeSpaceInEnergyStorage(IPowerProvider provider) {
        return provider.getEnergyStored() < provider.getMaxEnergyStored();
    }

    public static boolean gotFreeSpaceInEnergyStorageAndWantsEnergy(IPowerReceptor receptor, ForgeDirection from) {
        return gotFreeSpaceInEnergyStorage(receptor.getPowerProvider()) && doesWantEnergy(receptor, from);
    }

    public static boolean isPowerTile(IIntegerCoordinates position) {
        return isPowerTile(position.getTile());
    }

    public static boolean isPowerTile(TileEntity tile) {
        if (tile instanceof IPowerReceptor) {
            IPowerProvider receptor = ((IPowerReceptor) tile).getPowerProvider();
            return receptor != null;
        }

        return false;
    }

    public static boolean doesWantEnergy(IPowerReceptor receptor, ForgeDirection fromSide) {
        return receptor.powerRequest(fromSide) > 0;
    }
}
