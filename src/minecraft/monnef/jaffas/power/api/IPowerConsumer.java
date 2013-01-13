package monnef.jaffas.power.api;

public interface IPowerConsumer {
    void setPowerManager(IPowerConsumerManager manager);

    IPowerConsumerManager getPowerManager();
}
