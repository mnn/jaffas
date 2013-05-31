/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import monnef.core.utils.Interval;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static monnef.jaffas.technic.common.FungusInfo.NeedCompostEnum.NEVER;

public class FungiCatalog {
    private static final int TPS = 20;
    private static int maxId = 0;
    private static HashMap<Integer, Multiset<Integer>> blockIdToMushroom;

    public static HashMap<Integer, FungusInfo> catalog;

    public static final int PORCINO_ID = 1;
    public static final int PARASOL_ID = 2;

    static {
        catalog = new HashMap<Integer, FungusInfo>();
        createSpecie("Porcino", "Boletus Edulis", PORCINO_ID, 3, 10, 30, 1, 10, 2, 4, 1, 80, 2, 4, Interval.fromArray(1, 3, 3, 5, 3, 5, 3, 5));
        createSpecie("Parasol", "Macrolepiota Procera", PARASOL_ID, 3, 10, 30, 1, 10, 1, 3, 1, 80, 3, 4, Interval.fromArray(1, 3, 3, 5, 3, 5, 3, 5));

        blockIdToMushroom = new HashMap<Integer, Multiset<Integer>>();
        addMushroomDropFromBlock(Block.mushroomBrown.blockID, PORCINO_ID, 3);
        addMushroomDropFromBlock(Block.mushroomBrown.blockID, PARASOL_ID, 2);
    }

    public static void addMushroomDropFromBlock(int blockId, int mushroomId) {
        addMushroomDropFromBlock(blockId, mushroomId, 1);
    }

    public static void addMushroomDropFromBlock(int blockId, int mushroomId, int times) {
        if (!blockIdToMushroom.containsKey(blockId)) {
            blockIdToMushroom.put(blockId, HashMultiset.<Integer>create());
        }
        Multiset<Integer> recordForBlock = blockIdToMushroom.get(blockId);
        recordForBlock.add(mushroomId, times);
    }

    public static ItemStack getRandomDropFromBlock(int blockId) {
        // TODO: optimize?
        Multiset<Integer> found = blockIdToMushroom.get(blockId);
        if (found == null || found.size() == 0) return null;
        int i = JaffasFood.rand.nextInt(found.size());
        return get(found.toArray(new Integer[]{})[i]).createLootOneItem();
    }

    public static FungusInfo get(int id) {
        return catalog.get(id);
    }

    public static int getMaxId() {
        return maxId;
    }

    // times are in minutes
    private static FungusInfo createSpecie(String title, String subTitle, int id, int states, int minDie, int maxDie, int minSpore, int maxSpore, int minDrop, int maxDrop, int compostConsumptionSpeed, int surviveRateInPercent, int sporeTries, int compostGrowMultiplier, Interval[] stateLens) {
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
        info.compostConsumptionSpeed = compostConsumptionSpeed;
        // convert lengths of states from minutes and apply compost grow multiplier
        for (int i = 0; i < stateLens.length; i++) {
            stateLens[i] = new Interval(stateLens[i].getMin() * TPS * 60 * compostGrowMultiplier, stateLens[i].getMax() * TPS * 60 * compostGrowMultiplier);
        }
        info.stateLength = stateLens;
        info.title = title;
        info.subTitle = subTitle;
        info.id = id;
        info.dropCount = new Interval(minDrop, maxDrop);
        info.surviveRate = surviveRateInPercent;
        info.sporeTries = sporeTries;
        info.revertsWithoutCompost = false;
        info.setStateNeedCompostToGrow(NEVER, states);
        catalog.put(id, info);
        return info;
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
