/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    private ItemStack iconStack;

    public JaffaCreativeTab(String label) {
        super(label);
    }

    public void setup(JaffaItem iconItem) {
        iconStack = new ItemStack(ItemManager.getItem(iconItem));
    }

    public void setup(Item item) {
        iconStack = new ItemStack(item);
    }

    @Override
    public ItemStack getIconItemStack() {
        return iconStack;
    }
}
