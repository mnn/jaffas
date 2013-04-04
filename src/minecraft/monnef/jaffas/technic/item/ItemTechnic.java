package monnef.jaffas.technic.item;

import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;

public class ItemTechnic extends ItemJaffaBase {
    public ItemTechnic(int id, int textureIndex) {
        super(id, textureIndex);
        setCreativeTab(JaffasTechnic.CreativeTab);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 3;
    }
}
