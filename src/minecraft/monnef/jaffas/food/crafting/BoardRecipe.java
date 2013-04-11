/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.crafting;

import net.minecraft.item.ItemStack;

public class BoardRecipe {
    private final ItemStack _input;
    private final ItemStack _output;

    public BoardRecipe(ItemStack input, ItemStack output) {
        _input = input.copy();
        _output = output.copy();
    }

    public ItemStack getInput() {
        return _input;
    }

    public ItemStack getOutput() {
        return _output;
    }
}
