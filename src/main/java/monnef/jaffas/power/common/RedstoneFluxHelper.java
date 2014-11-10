/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import monnef.core.api.IIntegerCoordinates;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneFluxHelper {
    public static boolean gotFreeSpaceInEnergyStorage(IEnergyHandler receptor, ForgeDirection from) {
        return receptor.receiveEnergy(from, 1, true) > 0;
    }

    public static boolean gotFreeSpaceInEnergyStorageAndWantsEnergy(IEnergyHandler receptor, ForgeDirection from) {
        if (receptor == null) return false;
        return gotFreeSpaceInEnergyStorage(receptor, from) && doesWantEnergyFromDirection(receptor, from);
    }

    public static boolean isPowerTile(IIntegerCoordinates position) {
        return isPowerTile(position.getTile());
    }

    public static boolean isPowerTile(TileEntity tile) {
        return tile instanceof IEnergyConnection;
    }

    public static boolean doesWantEnergyFromDirection(IEnergyHandler receptor, ForgeDirection fromSide) {
        if (receptor == null) return false;
        return receptor.receiveEnergy(fromSide, 1, true) > 0;
    }
}
