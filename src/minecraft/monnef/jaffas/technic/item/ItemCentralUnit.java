package monnef.jaffas.technic.item;

public class ItemCentralUnit extends ItemTechnicMulti {
    public ItemCentralUnit(int id, int textureIndex) {
        super(id, textureIndex);
        setUnlocalizedName("centralUnit");
        setMaxStackSize(16);
        subNames = new String[]{"simple", "normal", "advanced"};
        subTitles = new String[]{"Simple Central Unit", "Central Unit", "Advanced Central Unit"};
    }
}
