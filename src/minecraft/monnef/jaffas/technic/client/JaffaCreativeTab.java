/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.technic.client;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    public JaffaCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(JaffasTechnic.jaffarrolRefined);
    }
}
