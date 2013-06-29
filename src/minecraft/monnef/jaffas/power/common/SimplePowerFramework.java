/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.PowerFramework;

public class SimplePowerFramework extends PowerFramework {
    @Override
    public IPowerProvider createPowerProvider() {
        return new SimplePowerProvider();
    }
}
