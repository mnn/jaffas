package monnef.jaffas.technic.item;

import monnef.jaffas.technic.jaffasTechnic;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;

public class ItemSwordTechnic extends ItemSword {
    public ItemSwordTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, material);
        setCreativeTab(jaffasTechnic.CreativeTab);
        //setIconIndex(textureOffset);
        //setTextureFile(jaffasTechnic.textureFile);
    }
}
