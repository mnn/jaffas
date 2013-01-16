package monnef.jaffas.power;

import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerProvider;
import net.minecraftforge.common.ForgeDirection;

public class PowerUtils {
    public static boolean Connect(IPowerProvider provider, IPowerConsumer consumer, ForgeDirection side) {
        boolean success = true;
        if (side == ForgeDirection.UNKNOWN) {
            success &= provider.getPowerManager().connect(consumer);
            consumer.getPowerManager().connect(provider);
        } else {
            success &= provider.getPowerManager().connectDirect(consumer, side);
            consumer.getPowerManager().connectDirect(provider, side);
        }

        return success;
    }

    public static void Disconnect(IPowerProvider provider, IPowerConsumer consumer) {
        provider.getPowerManager().disconnect(consumer);
        consumer.getPowerManager().disconnect();
    }
}
