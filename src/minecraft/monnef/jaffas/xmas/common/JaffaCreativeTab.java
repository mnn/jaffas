/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.xmas.common;


import monnef.jaffas.xmas.JaffasXmas;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    public JaffaCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(JaffasXmas.ItemGiantCandy);
    }
}
