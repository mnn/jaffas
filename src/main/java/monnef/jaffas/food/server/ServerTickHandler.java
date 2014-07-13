/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.server;

import cpw.mods.fml.relauncher.Side;
import monnef.core.common.ScheduledTicker;

public class ServerTickHandler extends ScheduledTicker {
    @Override
    public void onServerTickStart() {
        onTick();
    }

    private void onTick() {
    }

    @Override
    public int nextTickSpacing(Side side) {
        return 0;
    }
}
