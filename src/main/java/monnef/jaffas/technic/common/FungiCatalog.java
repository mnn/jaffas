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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

import static monnef.jaffas.technic.common.FungusInfo.NeedCompostEnum.NEVER;

public class FungiCatalog {
    private static final int TPS = 20;
    private static int maxId = 0;
    private static HashMap<Block, Multiset<Integer>> blockToMushroom;

    public static HashMap<Integer, FungusInfo> catalog;

    public static final int PORCINO_ID = 1;
    public static final int PARASOL_ID = 2;
    public static final int FLYAGARIC_ID = 3;

    private static boolean groupsRegistered;


    static {
        catalog = new HashMap<Integer, FungusInfo>();
        createSpecie("Porcino", "Boletus Edulis", PORCINO_ID, 3, 5, 30, 1, 10, 2, 4, 1, 90, 2, 4, Interval.fromArray(1, 3, 3, 5, 3, 5, 3, 5)).markEatableForRecipes();
        createSpecie("Parasol", "Macrolepiota Procera", PARASOL_ID, 3, 10, 35, 1, 10, 1, 3, 1, 80, 3, 4, Interval.fromArray(1, 3, 3, 5, 3, 5, 3, 5)).markEatableForRecipes();
        createSpecie("Fly Agaric", "Amanita Muscaria", FLYAGARIC_ID, 3, 15, 25, 3, 15, 1, 2, 2, 70, 2, 4, Interval.fromArray(2, 3, 1, 5, 1, 7, 1, 2));

        blockToMushroom = new HashMap<Block, Multiset<Integer>>();
        addMushroomDropFromBlock(Blocks.brown_mushroom, PORCINO_ID, 3);
        addMushroomDropFromBlock(Blocks.brown_mushroom, PARASOL_ID, 2);
        addMushroomDropFromBlock(Blocks.red_mushroom, FLYAGARIC_ID, 2);
    }

    public static void registerShroomGroups() {
        if (groupsRegistered) {
            throw new RuntimeException("Mushroom groups already registered!");
        }
        groupsRegistered = true;

        for (FungusInfo entry : catalog.values()) {
            if (entry.markedEatableForRecipes) {
                OreDictionary.registerOre(JaffasTechnic.MUSHROOMS_EATABLE, new ItemStack(JaffasTechnic.fungus, 1, entry.id));
            }
        }
    }

    public static void addMushroomDropFromBlock(Block block, int mushroomId) {
        addMushroomDropFromBlock(block, mushroomId, 1);
    }

    public static void addMushroomDropFromBlock(Block block, int mushroomId, int times) {
        if (!blockToMushroom.containsKey(block)) {
            blockToMushroom.put(block, HashMultiset.<Integer>create());
        }
        Multiset<Integer> recordForBlock = blockToMushroom.get(block);
        recordForBlock.add(mushroomId, times);
    }

    public static ItemStack getRandomDropFromBlock(Block block) {
        // TODO: optimize?
        Multiset<Integer> found = blockToMushroom.get(block);
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
            if (toCompare.getItem() == stack.getItem() && toCompare.getItemDamage() == stack.getItemDamage()) {
                return value;
            }
        }

        return null;
    }
}
