package monnef.jaffas.power.api;

public class PowerManager {
    private static IPowerConsumerManagerFactory factory;

    public boolean debug = true;

    private PowerManager() {
    }

    public static boolean IsInitialized() {
        return factory != null;
    }

    public static void InitializeFactory(IPowerConsumerManagerFactory newFactory) {
        if (factory != null) {
            throw new JaffasPowerException("Factory already initialized.");
        }

        factory = newFactory;
    }

    public static IPowerNode CreatePowerConsumerManager() {
        if (factory == null) {
            throw new JaffasPowerException("Factory is not initialized, is mod \"Jaffas and more!\" present?");
        }

        return factory.CreateManager();
    }
}
