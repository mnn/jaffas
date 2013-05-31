/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.utils.Interval;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.item.ItemStack;

public class FungusInfo {
    // 3x graphic states + one mycelium state (zero)
    public final int statesCount = 3;
    public Interval[] stateLength;
    public Interval timeToDie;
    public Interval sporeTime;
    public int compostConsumptionSpeed;
    public String title;
    public String subTitle;
    public boolean ordinalItemBind = true;
    public ItemStack specialItemBind = null;
    public Interval dropCount;
    public int id;
    public int surviveRate; // in percent
    public int sporeTries = 3;
    public Boolean[] stateNeedCompostToGrow;
    public boolean revertsWithoutCompost;
    public int growMultiplierOfCompost = 4;

    public ItemStack createLoot() {
        int count = dropCount.getRandom();
        if (count == 0) return null;

        ItemStack res;
        res = createLootOneItem();
        res.stackSize = count;
        return res;
    }

    public ItemStack createLootOneItem() {
        ItemStack res;
        if (ordinalItemBind) {
            res = new ItemStack(JaffasTechnic.fungus, 1, id);
        } else {
            res = specialItemBind.copy();
        }
        return res;
    }

    public FungusInfo setReverseWithoutCompost() {
        revertsWithoutCompost = true;
        return this;
    }

    /**
     * @param type   Type of preset to use.
     * @param states Number of visible mushroom states (not counting "zero" state).
     * @return This object.
     */
    public FungusInfo setStateNeedCompostToGrow(NeedCompostEnum type, int states) {
        stateNeedCompostToGrow = new Boolean[states + 1];

        if (type != NeedCompostEnum.ALL_STATES) {
            for (int i = 0; i < stateNeedCompostToGrow.length; i++) {
                stateNeedCompostToGrow[i] = false;
            }
        }

        switch (type) {
            case NEVER:
                break;

            case ONLY_ZERO_STATE:
                stateNeedCompostToGrow[0] = true;
                break;

            case ONLY_LAST_STATE:
                stateNeedCompostToGrow[states] = true;
                break;

            case ALL_STATES:
                for (int i = 0; i < stateNeedCompostToGrow.length; i++) {
                    stateNeedCompostToGrow[i] = true;
                }
                break;

            default:
                throw new RuntimeException("Unknown preset.");
        }

        return this;
    }

    public enum NeedCompostEnum {
        NEVER,
        ONLY_ZERO_STATE,
        ONLY_LAST_STATE,
        ALL_STATES,
    }
}
