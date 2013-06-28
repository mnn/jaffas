/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import net.minecraft.tileentity.TileEntity;

public class BuildCraftHelper {
    public static boolean gotFreeSpaceInEnergyStorage(IPowerProvider provider) {
        return provider.getEnergyStored() < provider.getMaxEnergyStored();
    }

    public static boolean isPowerTile(TileEntity tile) {
        if (tile instanceof IPowerReceptor) {
            IPowerProvider receptor = ((IPowerReceptor) tile).getPowerProvider();
            return receptor != null;
        }

        return false;
    }
}
