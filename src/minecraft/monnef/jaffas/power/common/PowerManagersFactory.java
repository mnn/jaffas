/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.power.common;

import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerManagersFactory;
import monnef.jaffas.power.api.IPowerProviderManager;

public class PowerManagersFactory implements IPowerManagersFactory {
    @Override
    public IPowerConsumerManager CreateConsumerManager() {
        return new PowerConsumerManager();
    }

    @Override
    public IPowerProviderManager CreateProviderManager() {
        return new PowerProviderManager();
    }
}
