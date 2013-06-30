/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import buildcraft.api.power.PowerProvider;

public class SimplePowerProvider extends PowerProvider {
    public SimplePowerProvider() {
        powerLoss = 0;
        powerLossRegularity = 100;
    }
}
