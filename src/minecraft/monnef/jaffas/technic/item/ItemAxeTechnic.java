package monnef.jaffas.technic.item;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemAxe;

public class ItemAxeTechnic extends ItemAxe {
    public ItemAxeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, material);
        setCreativeTab(JaffasTechnic.CreativeTab);
        //setIconIndex(textureOffset);
        //setTextureFile(JaffasTechnic.textureFile);
    }
}

