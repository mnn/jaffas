package monnef.jaffas.trees;

import monnef.jaffas.food.mod_jaffas;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    public JaffaCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(mod_jaffas.getJaffaItem(mod_jaffas.JaffaItem.oranges));
    }
}
