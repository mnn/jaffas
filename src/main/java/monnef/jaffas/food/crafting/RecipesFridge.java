/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class RecipesFridge {
    private static HashMap<Item, ItemStack> recipes = new HashMap<Item, ItemStack>();

    public static ItemStack getCopyOfResult(int id) {
        ItemStack res = recipes.get(id);
        return res != null ? res.copy() : null;
    }

    public static void addRecipe(Item item, ItemStack result) {
        addRecipe(item, result, -1);
    }

    public static void addRecipe(Item item, ItemStack result, int maxTemperature) {
        recipes.put(item, result);
        // TODO: temperature?
    }
}
