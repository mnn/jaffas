package monnef.jaffas.power;

import monnef.jaffas.power.api.IPowerConsumerManagerFactory;
import monnef.jaffas.power.api.IPowerNode;

public class PowerConsumerManagerFactory implements IPowerConsumerManagerFactory {
    @Override
    public IPowerNode CreateManager() {
        return new PowerConsumerManager();
    }
}
