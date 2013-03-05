package monnef.jaffas.food.common;

import monnef.jaffas.trees.mod_jaffas_trees;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class SeedsHelper {
    private static Random rand = new Random();

    public static ItemStack getRandomSeed() {
        ItemStack seed;
        if (rand.nextInt(2) == 0 || !ModuleManager.IsModuleEnabled(ModulesEnum.trees)) {
            seed = new ItemStack(Item.seeds);
        } else {
            seed = getRandomJaffaSeed();
        }
        return seed;
    }

    public static ItemStack getRandomJaffaSeed() {
        ItemStack seed;// choosing from our seeds (tree, bush)
        if (rand.nextBoolean()) {
            int type = rand.nextInt(mod_jaffas_trees.leavesTypesCount) + 1;
            seed = mod_jaffas_trees.getTreeSeeds(type);
        } else {
            int type = rand.nextInt(mod_jaffas_trees.BushesList.size());
            seed = new ItemStack(mod_jaffas_trees.BushesList.get(mod_jaffas_trees.bushType.values()[type]).itemSeeds);
        }
        return seed;
    }
}
