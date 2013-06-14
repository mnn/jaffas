/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.utils.Interval;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

import static monnef.jaffas.technic.common.HighPlantLifeCycleDescriptor.LifeCycleType.ORDINAL;

public class HighPlantCatalog {
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
}
