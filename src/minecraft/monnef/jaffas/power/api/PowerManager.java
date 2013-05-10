/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.api;

public class PowerManager {
    private static IPowerManagersFactory factory;

    public boolean debug = true;

    // this is a "static" class, do not let anyone make an instance of me
    private PowerManager() {
    }

    public static boolean IsInitialized() {
        return factory != null;
    }

    public static void InitializeFactory(IPowerManagersFactory newFactory) {
        if (factory != null) {
            throw new JaffasPowerException("Factory already initialized.");
        }

        factory = newFactory;
    }

    public static IPowerConsumerManager CreatePowerConsumerManager() {
        checkFactory();
        return factory.CreateConsumerManager();
    }

    public static IPowerProviderManager CreatePowerProviderManager() {
        checkFactory();
        return factory.CreateProviderManager();
    }

    private static void checkFactory() {
        if (factory == null) {
            throw new JaffasPowerException("Factory is not initialized, is mod \"Jaffas and more!\" present?");
        }
    }
}
