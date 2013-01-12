package monnef.jaffas.food.crafting;

public class PersistentItemInfo {
    public int ItemID;
    public boolean Damage = false;
    public int SubstituteItemID = -1;

    public PersistentItemInfo(int ID) {
        this.ItemID = ID;
    }

    public PersistentItemInfo SetDamageCopies() {
        this.Damage = true;
        return this;
    }

    public PersistentItemInfo SetSubstituteItem(int ID) {
        this.SubstituteItemID = ID;
        return this;
    }
}
