/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

public class PersistentItemInfo {
    public int itemID;
    public boolean damage = false;
    public int substituteItemID = -1;
    public int substituteItemsCount = 1;

    public PersistentItemInfo(int ID) {
        this.itemID = ID;
    }

    public PersistentItemInfo SetDamageCopies() {
        this.damage = true;
        return this;
    }

    public PersistentItemInfo SetSubstituteItem(int ID) {
        this.substituteItemID = ID;
        return this;
    }

    public PersistentItemInfo SetSubstituteItemsCount(int count) {
        this.substituteItemsCount = count;
        return this;
    }
}
