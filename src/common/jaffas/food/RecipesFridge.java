package jaffas.food;

import net.minecraft.src.ItemStack;

import java.util.HashMap;

public class RecipesFridge {
    private static HashMap<Integer, ItemStack> recipes = new HashMap<Integer, ItemStack>();

    public static ItemStack getCopyOfResult(int id) {
        ItemStack res = recipes.get(id);
        return res != null ? res.copy() : null;
    }

    public static void AddRecipe(int id, ItemStack result) {
        recipes.put(id, result);
    }

    public static void AddRecipe(int id, ItemStack result, int maxTemperature) {
        AddRecipe(id, result, -1);
    }
}
