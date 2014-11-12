/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneFluxHelper {
    public static boolean gotFreeSpaceInEnergyStorage(IEnergyReceiver receiver, ForgeDirection from) {
        return receiver.receiveEnergy(from, 1, true) > 0;
    }

    public static boolean gotFreeSpaceInEnergyStorageAndWantsEnergy(IEnergyReceiver receiver, ForgeDirection from) {
        if (receiver == null) return false;
        return gotFreeSpaceInEnergyStorage(receiver, from) && doesWantEnergyFromDirection(receiver, from);
    }

    public static boolean isTilePowerReceiver(TileEntity tile) {
        return tile instanceof IEnergyReceiver;
    }

    public static boolean doesWantEnergyFromDirection(IEnergyReceiver receiver, ForgeDirection fromSide) {
        if (receiver == null) return false;
        return receiver.receiveEnergy(fromSide, 1, true) > 0;
    }
}
