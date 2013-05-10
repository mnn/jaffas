/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.api;

import monnef.core.api.IIntegerCoordinates;

public interface IPowerNodeCoordinates extends IIntegerCoordinates {
    IPowerNodeManager asNode();

    IPowerProvider asProvider();

    IPowerConsumer asConsumer();
}
