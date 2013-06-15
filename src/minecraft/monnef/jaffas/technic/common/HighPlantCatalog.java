/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.utils.Interval;
import monnef.core.utils.ItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

import static monnef.jaffas.technic.common.HighPlantLifeCycleDescriptor.LifeCycleType.ORDINAL;

public class HighPlantCatalog {
    public static final int BLANK_ID = 0;

    private static final HashMap<Integer, HighPlantInfo> catalog;
    private static final int HOP_ID = 1;

    static {
        catalog = new HashMap<Integer, HighPlantInfo>();

        addPlant(HOP_ID, new ItemStack(Item.appleRed), new ItemStack(Item.appleGold), 1, 5, HopRenderer.class, HighPlantLifeCycleDescriptor.create(ORDINAL));
    }

    private static HighPlantInfo addPlant(int id, ItemStack seed, ItemStack fruit, int fruitCountMin, int fruitCountMax, Class<? extends IHighPlantModel> modelClass, IHighPlantLifeCycleDescriptor lifeCycle) {
        if (id <= 0 || seed == null || fruit == null || fruitCountMin > fruitCountMax || modelClass == null) {
            throw new RuntimeException("illegal parameter(s)");
        }

        HighPlantInfo info = new HighPlantInfo();
        info.id = id;
        info.seed = seed.copy();
        info.fruit = fruit.copy();
        info.fruitCount = new Interval(fruitCountMin, fruitCountMax);
        info.renderer = modelClass;
        info.lifeCycle = lifeCycle;
        catalog.put(id, info);
        return info;
    }

    public static HighPlantInfo getPlant(int id) {
        return catalog.get(id);
    }

    public static boolean isBlank(int id) {
        return id == BLANK_ID;
    }

    // TODO: optimize?
    public static HighPlantInfo getPlantBySeedItem(ItemStack seed) {
        if (seed == null) return null;

        for (HighPlantInfo info : catalog.values()) {
            if (ItemHelper.haveStacksSameIdAndDamage(info.seed, seed)) {
                return info;
            }
        }

        return null;
    }
}
