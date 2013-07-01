/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import net.minecraft.item.ItemStack;

public interface IProcessingRecipeHandler {
    IProcessingRecipe findByInput(ItemStack stack);
}
