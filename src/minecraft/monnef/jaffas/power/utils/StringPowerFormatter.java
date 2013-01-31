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

// TODO optimize!
public class StringPowerFormatter {
    public static String getConnectionInfo(IPowerNode node, boolean debug) {
        if (node instanceof IPowerProviderManager)
            return getConnectionInfo((IPowerProviderManager) node, debug);
        else if (node instanceof IPowerConsumerManager)
            return getConnectionInfo((IPowerConsumerManager) node, debug);

        throw new RuntimeException("invalid power node");
    }

    public static String getConnectionInfo(IPowerConsumerManager consumer, boolean debug) {
        StringBuilder s = new StringBuilder();
        IPowerProvider provider = consumer.getProvider();
        if (provider != null) {
            s.append(TileEntityHelper.getFormattedCoordinates(provider.getPowerProviderManager().getTile()));
        } else {
            s.append("-");
        }

        return s.toString();
    }

    public static String getConnectionInfo(IPowerProviderManager provider, boolean debug) {
        List<String> list = new ArrayList<String>();
        boolean[] sides = provider.constructConnectedSides();
        for (int i = 0; i < sides.length; i++) {
            StringBuilder s = new StringBuilder();
            if (sides[i]) {
                if (debug) {
                    s.append(TileEntityHelper.getFormattedCoordinates((TileEntity) provider.getConsumer(ForgeDirection.getOrientation(i))));
                } else {
                    s.append(ForgeDirection.values()[i]);
                }
            } else {
                s.append("-");
            }
            list.add(s.toString());
        }

        return Joiner.on("|").join(list);
    }

    public static String getEnergyInfo(boolean provider, int buffer, int maxBuff, int packetSize) {
        StringBuilder s = new StringBuilder();
        s.append(provider ? "P>" : ">C");
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
