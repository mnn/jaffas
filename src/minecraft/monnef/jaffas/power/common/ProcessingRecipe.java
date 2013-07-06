/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import monnef.core.utils.ItemHelper;
import net.minecraft.item.ItemStack;

public class ProcessingRecipe implements IProcessingRecipe {
    private ItemStack[] input;
    private ItemStack[] output;
    private int duration;

    public ProcessingRecipe(ItemStack[] input, ItemStack[] output, int duration) {
        this.input = input;
        this.output = output;
        this.duration = duration;
    }

    @Override
    public ItemStack[] getInput() {
        return input;
    }

    @Override
    public ItemStack[] getOutput() {
        return output;
    }

    @Override
    public boolean doesInputMatch(ItemStack[] testedInv) {
        if (testedInv == null || testedInv.length != input.length) return false;

        for (int i = 0; i < input.length; i++) {
            ItemStack template = input[i];
            ItemStack testedStack = testedInv[i];
            if (template == null && testedStack == null) continue;
            if (!ItemHelper.haveStacksSameIdDamageAndProperSize(template, testedStack)) return false;
        }

        return true;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
