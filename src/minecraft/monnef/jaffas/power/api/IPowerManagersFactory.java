/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.power.api;

public interface IPowerManagersFactory {
    IPowerConsumerManager CreateConsumerManager();

    IPowerProviderManager CreateProviderManager();
}
