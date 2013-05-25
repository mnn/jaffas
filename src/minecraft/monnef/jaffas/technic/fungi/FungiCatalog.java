/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.fungi;

import monnef.core.utils.Interval;

import java.util.HashMap;

public class FungiCatalog {
    private static final int TPS = 20;
    private static int maxId = 0;

    public static HashMap<Integer, FungusInfo> catalog;

    static {
        catalog = new HashMap<Integer, FungusInfo>();

        createSpecie("Porcino", "Boletus Edulis", 1, 3, 10, 30, 1, 10, 60, 120, Interval.fromArray(1, 3, 3, 5, 3, 5, 3, 5));
    }

    public static FungusInfo get(int id) {
        return catalog.get(id);
    }

    public static int getMaxId() {
        return maxId;
    }

    // times are in minutes
    private static void createSpecie(String title, String subTitle, int id, int states, int minDie, int maxDie, int minSpore, int maxSpore, int minHumus, int maxHumus, Interval[] stateLens) {
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
        info.humusLastFor = new Interval(TPS * 60 * minHumus, TPS * 60 * maxHumus);
        // convert lengths of states from minutes
        for (int i = 0; i < stateLens.length; i++) {
            stateLens[i] = new Interval(stateLens[i].getMin() * TPS * 60, stateLens[i].getMax() * TPS * 60);
        }
        info.stateLength = stateLens;
        info.title = title;
        info.subTitle = subTitle;
        catalog.put(id, info);
    }

}
