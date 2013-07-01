/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.item.ItemStack;

public class SimpleProcessingRecipeHandler implements IProcessingRecipeHandler {
    // item id, meta, recipe
    private Table<Integer, Integer, IProcessingRecipe> recipes;

    public SimpleProcessingRecipeHandler() {
        recipes = HashBasedTable.create();
    }

    public void addRecipe(ItemStack input, ItemStack output, int duration) {
        SimpleProcessingRecipe recipe = new SimpleProcessingRecipe(input.copy(), output.copy(), duration);
        recipes.put(input.itemID, input.getItemDamage(), recipe);
    }

    @Override
    public IProcessingRecipe findByInput(ItemStack stack) {
        if (stack == null) return null;
        IProcessingRecipe found = recipes.get(stack.itemID, stack.getItemDamage());
        if (found.doesInputMatch(stack)) return found;
        return null;
    }
}
