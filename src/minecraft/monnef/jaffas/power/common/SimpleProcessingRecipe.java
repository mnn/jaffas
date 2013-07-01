/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import monnef.core.utils.ItemHelper;
import net.minecraft.item.ItemStack;

public class SimpleProcessingRecipe implements IProcessingRecipe {
    private ItemStack input;
    private ItemStack output;
    private int duration;

    public SimpleProcessingRecipe(ItemStack input, ItemStack output, int duration) {
        this.input = input;
        this.output = output;
        this.duration = duration;
    }

    @Override
    public ItemStack getInput() {
        return input;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean doesInputMatch(ItemStack input) {
        return ItemHelper.haveStacksSameIdDamageAndProperSize(this.input, input);
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
