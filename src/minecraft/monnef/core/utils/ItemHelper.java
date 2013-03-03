package monnef.core.utils;

import net.minecraft.item.ItemStack;

public class ItemHelper {
    /**
     * Damages item.
     * @param item
     * @param amount
     * @return If item is destroyed.
     */
    public static boolean DamageItem(ItemStack item, int amount) {
        //this.itemDamage > this.getMaxDamage()
        if (item == null) return false;
        if (amount <= 0) return false;

        int newItemDamage = item.getItemDamage() + amount;
        item.setItemDamage(newItemDamage);
        if (newItemDamage > item.getMaxDamage()) {
            return true;
        }

        return false;
    }
}
