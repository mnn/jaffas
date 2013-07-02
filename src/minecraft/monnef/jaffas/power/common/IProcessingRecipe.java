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
}
