package monnef.core.utils;

import net.minecraft.tileentity.TileEntity;

public class TileEntityHelper {
    public static String getFormattedCoordinates(TileEntity machine) {
        StringBuilder s = new StringBuilder();
        s.append(machine.xCoord);
        s.append("x");
        s.append(machine.yCoord);
        s.append("x");
        s.append(machine.zCoord);
        return s.toString();
    }
}
