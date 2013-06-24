/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class SeedsHelper {
    private static Random rand = new Random();

    public static ItemStack getRandomSeed() {
        ItemStack seed;
        if (rand.nextInt(2) == 0 || !ModuleManager.isModuleEnabled(ModulesEnum.trees)) {
            seed = new ItemStack(Item.seeds);
        } else {
            seed = getRandomJaffaSeed();
        }
        return seed;
    }

    public static ItemStack getRandomJaffaSeed() {
        ItemStack seed;// choosing from our seeds (tree, bush)
        if (rand.nextInt(30) == 0) {
            seed = new ItemStack(JaffasTechnic.hopSeeds);
        } else {
            if (rand.nextBoolean()) {
                int type = rand.nextInt(JaffasTrees.leavesTypesCount) + 1;
                seed = JaffasTrees.getTreeSeeds(type);
            } else {
                int type = rand.nextInt(JaffasTrees.BushesList.size());
                seed = new ItemStack(JaffasTrees.BushesList.get(JaffasTrees.bushType.values()[type]).itemSeeds);
            }
        }

        return seed;
    }
}
