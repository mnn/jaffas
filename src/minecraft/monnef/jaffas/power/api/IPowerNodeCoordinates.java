package monnef.jaffas.power.api;

import monnef.core.api.IIntegerCoordinates;

public interface IPowerNodeCoordinates extends IIntegerCoordinates {
    IPowerNode asNode();

    IPowerProvider asProvider();

    IPowerConsumer asConsumer();
}
