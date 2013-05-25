/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.fungi;

import monnef.core.utils.Interval;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FungiCatalog {
    private static final int TPS = 20;
    private static int maxId = 0;

    public static HashMap<Integer, FungusInfo> catalog;

    static {
        catalog = new HashMap<Integer, FungusInfo>();

        createSpecie("Porcino", "Boletus Edulis", 1, 3, 10, 30, 1, 10, 2, 4, 1, 80, 2, Interval.fromArray(1, 3, 3, 5, 3, 5, 3, 5));
        createSpecie("Parasol", "Macrolepiota procera", 2, 3, 10, 30, 1, 10, 1, 3, 1, 80, 3, Interval.fromArray(1, 3, 3, 5, 3, 5, 3, 5));
    }

    public static FungusInfo get(int id) {
        return catalog.get(id);
    }

    public static int getMaxId() {
        return maxId;
    }

    // times are in minutes
    private static void createSpecie(String title, String subTitle, int id, int states, int minDie, int maxDie, int minSpore, int maxSpore, int minDrop, int maxDrop, int humusConsumptionSpeed, int surviveRateInPercent, int sporeTries, Interval[] stateLens) {
        if (id == 0) {
            throw new RuntimeException("Inserting fungus with ZERO id!");
        }
        if (catalog.containsKey(id)) {
            throw new RuntimeException("Overriding fungus type!");
        }
        if (states + 1 != stateLens.length) {
            throw new RuntimeException("Number of states doesn't match durations of states.");
        }

        if (maxId < id) maxId = id;

        FungusInfo info = new FungusInfo();
        info.timeToDie = new Interval(TPS * 60 * minDie, TPS * 60 * maxDie);
        info.sporeTime = new Interval(TPS * 60 * minSpore, TPS * 60 * maxSpore);
        info.humusConsumptionSpeed = humusConsumptionSpeed;
        // convert lengths of states from minutes
        for (int i = 0; i < stateLens.length; i++) {
            stateLens[i] = new Interval(stateLens[i].getMin() * TPS * 60, stateLens[i].getMax() * TPS * 60);
        }
        info.stateLength = stateLens;
        info.title = title;
        info.subTitle = subTitle;
        info.id = id;
        info.dropCount = new Interval(minDrop, maxDrop);
        info.surviveRate = surviveRateInPercent;
        info.sporeTries = sporeTries;
        catalog.put(id, info);
    }

    // TODO: optimize?
    public static FungusInfo findByDrop(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        for (Map.Entry<Integer, FungusInfo> item : catalog.entrySet()) {
            ItemStack toCompare;
            FungusInfo value = item.getValue();
            if (value.ordinalItemBind) {
                toCompare = new ItemStack(JaffasTechnic.fungus, 1, value.id);
            } else {
                toCompare = value.specialItemBind.copy();
            }
            if (toCompare.itemID == stack.itemID && toCompare.getItemDamage() == stack.getItemDamage()) {
                return value;
            }
        }

        return null;
    }
}
