package monnef.jaffas.power;

import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerConsumerManagerFactory;

public class PowerConsumerManagerFactory implements IPowerConsumerManagerFactory {
    @Override
    public IPowerConsumerManager CreateManager() {
        return new PowerConsumerManager();
    }
}
