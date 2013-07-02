/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import net.minecraft.item.ItemStack;

public interface IProcessingRecipeHandler {
    IProcessingRecipe findByInput(ItemStack[] stack);

    void addRecipe(ItemStack[] input, ItemStack[] output, int duration);
}
