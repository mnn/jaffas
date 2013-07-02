/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static monnef.core.utils.ItemHelper.copyStackArray;

public class SimpleProcessingRecipeHandler implements IProcessingRecipeHandler {
    // could be rewritten to use e.g. trie,
    // but so far it doesn't seem to be a performance issue
    // (it's only called when processing is starting and ending)

    private List<IProcessingRecipe> recipes;

    public SimpleProcessingRecipeHandler() {
        recipes = new ArrayList<IProcessingRecipe>();
    }

    @Override
    public void addRecipe(ItemStack[] input, ItemStack[] output, int duration) {
        SimpleProcessingRecipe recipe = new SimpleProcessingRecipe(copyStackArray(input), copyStackArray(output), duration);
        recipes.add(recipe);
    }

    @Override
    public IProcessingRecipe findByInput(ItemStack[] inv) {
        if (inv == null) return null;

        for (IProcessingRecipe found : recipes) {
            if (found.doesInputMatch(inv)) return found;
        }
        return null;
    }
}
