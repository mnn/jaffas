/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import net.minecraft.item.ItemStack;

public interface IProcessingRecipe {
    public ItemStack[] getInput();

    public ItemStack[] getOutput();

    public boolean doesInputMatch(ItemStack[] input);

    public int getDuration();

    public IProcessingRecipe copy();

    public boolean isAnyInput(ItemStack test);

    public boolean isAnyOutput(ItemStack test);

    boolean isAny(ItemStack test, RecipeItemType type);
}
