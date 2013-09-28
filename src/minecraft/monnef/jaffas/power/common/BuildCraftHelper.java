/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import monnef.core.api.IIntegerCoordinates;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class BuildCraftHelper {
    public static boolean gotFreeSpaceInEnergyStorage(IPowerReceptor receptor, ForgeDirection from) {
        return gotFreeSpaceInEnergyStorage(receptor.getPowerReceiver(from));
    }

    public static boolean gotFreeSpaceInEnergyStorage(PowerHandler.PowerReceiver receiver) {
        return receiver.getEnergyStored() < receiver.getMaxEnergyStored();
    }

    public static boolean gotFreeSpaceInEnergyStorageAndWantsEnergy(PowerHandler.PowerReceiver receiver) {
        return gotFreeSpaceInEnergyStorage(receiver) && doesWantEnergy(receiver);
    }

    public static boolean gotFreeSpaceInEnergyStorageAndWantsEnergy(IPowerReceptor receptor, ForgeDirection from) {
        PowerHandler.PowerReceiver receiver = receptor.getPowerReceiver(from);
        return gotFreeSpaceInEnergyStorage(receiver) && doesWantEnergy(receiver);
    }

    public static boolean isPowerTile(IIntegerCoordinates position) {
        return isPowerTile(position.getTile());
    }

    public static boolean isPowerTile(TileEntity tile) {
        return tile instanceof IPowerReceptor;
    }

    public static boolean doesWantEnergyFromDirection(IPowerReceptor receptor, ForgeDirection fromSide) {
        //return receptor.powerRequest(fromSide) > 0;
        PowerHandler.PowerReceiver receiver = receptor.getPowerReceiver(fromSide);
        return doesWantEnergy(receiver);
    }

    public static boolean doesWantEnergy(PowerHandler.PowerReceiver receiver) {
        return receiver.powerRequest() > 0;
    }
}
