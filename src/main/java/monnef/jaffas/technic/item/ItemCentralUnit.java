/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

public class ItemCentralUnit extends ItemTechnicMulti {
    private static final String[] subNames = {"simple", "normal", "advanced"};
    private static final String[] subTitles = {"Simple Central Unit", "Central Unit", "Advanced Central Unit"};

    @Override
    public String[] getSubNames() {
        return subNames;
    }

    @Override
    public String[] getSubTitles() {
        return subTitles;
    }

    public ItemCentralUnit( int textureIndex) {
        super(textureIndex);
        setUnlocalizedName("centralUnit");
        setMaxStackSize(16);
    }
}
