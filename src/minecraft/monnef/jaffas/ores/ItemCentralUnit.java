package monnef.jaffas.ores;

public class ItemCentralUnit extends ItemOresMulti {
    public ItemCentralUnit(int id, int textureIndex) {
        super(id, textureIndex);
        setItemName("centralUnit");
        setMaxStackSize(16);
        subNames = new String[]{"simple", "normal", "advanced"};
        subTitles = new String[]{"Simple Central Unit", "Central Unit", "Advanced Central Unit"};
    }
}
