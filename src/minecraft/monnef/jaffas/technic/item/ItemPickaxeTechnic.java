package monnef.jaffas.technic.item;

import monnef.jaffas.technic.jaffasTechnic;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;

public class ItemPickaxeTechnic extends ItemPickaxe {
    public ItemPickaxeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, material);
        setCreativeTab(jaffasTechnic.CreativeTab);
        //setIconIndex(textureOffset);
        //setTextureFile(jaffasTechnic.textureFile);
    }
}
