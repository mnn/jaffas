package monnef.jaffas.ores;

public class ItemCasing extends ItemOresMulti {
    public ItemCasing(int id, int textureIndex) {
        super(id, textureIndex);
        subNames = new String[]{"normal", "refined"};
        subTitles = new String[]{"Casing", "Refined Casing"};
    }
}
