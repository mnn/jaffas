/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.crafting;


import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class RecipesFridge {
    private static HashMap<Integer, ItemStack> recipes = new HashMap<Integer, ItemStack>();

    public static ItemStack getCopyOfResult(int id) {
        ItemStack res = recipes.get(id);
        return res != null ? res.copy() : null;
    }

    public static void AddRecipe(int id, ItemStack result) {
        AddRecipe(id, result, -1);
    }

    public static void AddRecipe(int id, ItemStack result, int maxTemperature) {
        recipes.put(id, result);
        // TODO: temperature?
    }
}
