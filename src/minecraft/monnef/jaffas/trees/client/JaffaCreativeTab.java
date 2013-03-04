package monnef.jaffas.trees.client;

import monnef.jaffas.food.item.ItemManager;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    public JaffaCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ItemManager.getItem(JaffaItem.oranges));
    }
}
