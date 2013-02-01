package monnef.jaffas.power.utils;

import com.google.common.base.Joiner;
import monnef.core.TileEntityHelper;
import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerNode;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class StringPowerFormatter {
    public static String getConnectionInfo(IPowerNode node, boolean debug) {
        StringBuilder ret = new StringBuilder();

        if (node instanceof IPowerProviderManager) {
            ret.append(getConnectionInfo((IPowerProviderManager) node, debug));
        }

        if (node instanceof IPowerConsumerManager) {
            if (ret.length() > 0) ret.append(" ");
            ret.append(getConnectionInfo((IPowerConsumerManager) node, debug));
        }

        return ret.toString();
    }

    public static String getConnectionInfo(IPowerConsumerManager consumer, boolean debug) {
        StringBuilder s = new StringBuilder();
        IPowerProvider provider = consumer.getProvider();
        if (provider != null) {
            if (debug) {
                s.append(TileEntityHelper.getFormattedCoordinates(provider.getPowerProviderManager().getTile()));
            } else {
                s.append("wC");
            }
        } else {
            s.append("--");
        }

        return s.toString();
    }

    public final static String[] directionStrings = new String[]{"B", "U", "N", "S", "W", "E", "?"};

    public static String getConnectionInfo(IPowerProviderManager provider, boolean debug) {
        List<String> list = new ArrayList<String>();
        boolean[] sides = provider.constructConnectedSides();
        for (int i = 0; i < sides.length; i++) {
            StringBuilder s = new StringBuilder();
            if (sides[i]) {
                if (debug) {
                    s.append(TileEntityHelper.getFormattedCoordinates((TileEntity) provider.getConsumer(ForgeDirection.getOrientation(i))));
                } else {
                    s.append(directionStrings[i]);
                }
            } else {
                if (debug) {
                    s.append("-");
                }
            }

            String s1 = s.toString();
            if (!s1.isEmpty()) {
                list.add(s1);
            }
        }

        return Joiner.on("|").join(list);
    }

    public static String getEnergyInfo(boolean provider, boolean consumer, int buffer, int maxBuff, int packetSize) {
        StringBuilder s = new StringBuilder();
        s.append(provider && consumer ? ">B>" : (provider ? " P>" : ">C "));
        s.append(":");
        s.append(buffer);
        s.append("/");
        s.append(maxBuff);
        s.append("(");
        s.append(packetSize);
        s.append(")");
        return s.toString();
    }
}
