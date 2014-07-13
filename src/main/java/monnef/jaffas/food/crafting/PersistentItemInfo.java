/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import net.minecraft.item.Item;

public class PersistentItemInfo {
    public Item item;
    public boolean damage = false;
    public Item substituteItem;
    public int substituteItemsCount = 1;

    public PersistentItemInfo(Item item) {
        this.item = item;
    }

    public PersistentItemInfo setDamageCopies() {
        this.damage = true;
        return this;
    }

    public PersistentItemInfo setSubstituteItem(Item item) {
        this.substituteItem = item;
        return this;
    }

    public PersistentItemInfo setSubstituteItemsCount(int count) {
        this.substituteItemsCount = count;
        return this;
    }
}
