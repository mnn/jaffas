package monnef.jaffas.technic.item;

import monnef.jaffas.technic.mod_jaffas_technic;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;

public class ItemPickaxeTechnic extends ItemPickaxe {
    public ItemPickaxeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, material);
        setCreativeTab(mod_jaffas_technic.CreativeTab);
        setIconIndex(textureOffset);
        setTextureFile(mod_jaffas_technic.textureFile);
    }
}
