/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.fungi;

import monnef.core.utils.Interval;

public class FungusInfo {
    // 3x graphic states + one mycelium state (zero)
    public final int statesCount = 3;
    public Interval[] stateLength;
    public Interval timeToDie;
    public Interval sporeTime;
    public Interval humusLastFor;
}
