/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.fungi;

import monnef.core.utils.Interval;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.item.ItemStack;

public class FungusInfo {
    // 3x graphic states + one mycelium state (zero)
    public final int statesCount = 3;
    public Interval[] stateLength;
    public Interval timeToDie;
    public Interval sporeTime;
    public int humusConsumptionSpeed;
    public String title;
    public String subTitle;
    public boolean ordinalItemBind = true;
    public ItemStack specialItemBind = null;
    public Interval dropCount;
    public int id;
    public int surviveRate; // in percent
    public int sporeTries = 3;

    public ItemStack createLoot() {
        int count = dropCount.getRandom();
        if (count == 0) return null;
        if (ordinalItemBind) {
            return new ItemStack(JaffasTechnic.fungus, count, id);
        } else {
            ItemStack res = specialItemBind.copy();
            res.stackSize = count;
            return res;
        }
    }
}
