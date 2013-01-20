package monnef.jaffas.carts.client;

import monnef.jaffas.carts.mod_jaffas_carts;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    public JaffaCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(mod_jaffas_carts.itemLocomotive);
    }
}
