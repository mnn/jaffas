package monnef.jaffas.power;

import monnef.jaffas.power.api.*;
import net.minecraftforge.common.ForgeDirection;

public class PowerUtils {
    public static final int LOSE_COEFFICIENT_MULTIPLIER = 100;

    public static boolean connect(IPowerNodeCoordinates provider, IPowerNodeCoordinates consumer, ForgeDirection providersSide) {
        // TODO check coords and capabilities?
        if (provider == null || consumer == null || providersSide == null) {
            return false;
        }

        if (providersSide == ForgeDirection.UNKNOWN) {
            return connectRemote(provider, consumer);
        } else {
            return connectDirect(provider, consumer, providersSide);
        }
    }

    private static boolean connectDirect(IPowerNodeCoordinates provider, IPowerNodeCoordinates consumer, ForgeDirection providersSide) {
        boolean success;
        IPowerProviderManager providerManager = provider.asProvider().getPowerProviderManager();
        success = providerManager != null ? providerManager.connectDirect(consumer, providersSide) : false;

        IPowerConsumerManager consumerManager = consumer.asConsumer().getPowerConsumerManager();
        success &= consumerManager != null;
        if (success) consumerManager.connectDirect(provider, providersSide.getOpposite());
        return success;
    }

    private static boolean connectRemote(IPowerNodeCoordinates provider, IPowerNodeCoordinates consumer) {
        boolean success;
        success = provider.asProvider().getPowerProviderManager().connect(consumer);
        consumer.asConsumer().getPowerConsumerManager().connect(provider);
        return success;
    }

    public static void disconnect(IPowerNodeCoordinates provider, IPowerNodeCoordinates consumer) {
        if (provider == null || consumer == null) return;

        IPowerProvider powerProvider = provider.asProvider();
        if (powerProvider != null) {
            IPowerProviderManager providerManager = powerProvider.getPowerProviderManager();
            if (providerManager != null) {
                providerManager.disconnect(consumer);
            }
        }

        IPowerConsumer powerConsumer = consumer.asConsumer();
        if (powerConsumer != null) {
            IPowerConsumerManager consumerManager = powerConsumer.getPowerConsumerManager();
            if (consumerManager != null) {
                consumerManager.disconnect();
            }
        }
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
