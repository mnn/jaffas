/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import net.minecraft.item.ItemStack;

import java.util.Collection;

public interface IProcessingRecipeHandler {
    IProcessingRecipe findByInput(ItemStack[] stack);

    void addRecipe(ItemStack[] input, ItemStack[] output, int duration);

    Collection<IProcessingRecipe> copyRecipes();
}
