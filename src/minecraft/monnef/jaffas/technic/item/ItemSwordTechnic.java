package monnef.jaffas.technic.item;

import monnef.jaffas.food.item.ItemJaffaSword;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.item.EnumToolMaterial;

public class ItemSwordTechnic extends ItemJaffaSword {
    public ItemSwordTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
        setCreativeTab(JaffasTechnic.CreativeTab);
        setSheetNumber(3);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }
}
