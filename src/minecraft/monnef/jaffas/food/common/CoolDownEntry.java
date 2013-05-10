/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.core.utils.TimeUtils;

public class CoolDownEntry {
    private long coolDownEnd = 0;

    public CoolDownEntry() {
    }

    public int getRemainingCoolDown() {
        int time = (int) (coolDownEnd - TimeUtils.getCurrentTimeInSeconds());
        return time >= 0 ? time : 0;
    }

    public boolean isCoolDownActive() {
        return getRemainingCoolDown() > 0;
    }

    public void setCoolDown(int coolDownInSeconds) {
        coolDownEnd = TimeUtils.getCurrentTimeInSeconds() + coolDownInSeconds;
    }

    public void synchronizeCoolDown(int data) {
        coolDownEnd = data;
    }
}
