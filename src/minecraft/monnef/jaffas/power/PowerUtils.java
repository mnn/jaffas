package monnef.jaffas.power;

import monnef.jaffas.power.api.IPowerNodeCoordinates;
import net.minecraftforge.common.ForgeDirection;

public class PowerUtils {
    public static final int LOSE_COEFFICIENT_MULTIPLIER = 100;

    public static boolean Connect(IPowerNodeCoordinates provider, IPowerNodeCoordinates consumer, ForgeDirection providersSide) {
        boolean success = true;
        if (providersSide == ForgeDirection.UNKNOWN) {
            success &= provider.asProvider().getPowerProviderManager().connect(consumer);
            consumer.asConsumer().getPowerConsumerManager().connect(provider);
        } else {
            success &= provider.asProvider().getPowerProviderManager().connectDirect(consumer, providersSide);
            consumer.asConsumer().getPowerConsumerManager().connectDirect(provider, providersSide.getOpposite());
        }

        return success;
    }

    public static void Disconnect(IPowerNodeCoordinates provider, IPowerNodeCoordinates consumer) {
        if (provider == null || consumer == null) return;
        provider.asProvider().getPowerProviderManager().disconnect(consumer);
        consumer.asConsumer().getPowerConsumerManager().disconnect();
    }

    public static int loseEnergy(int energy, int distance) {
        return (energy * getLoseCoefficient(distance)) / LOSE_COEFFICIENT_MULTIPLIER;
    }

    public static int getLoseCoefficient(int distance) {
        if (distance <= 10) return 100;
        if (distance <= 20) return 80;
        if (distance <= 30) return 40;
        return 0;
    }
}
