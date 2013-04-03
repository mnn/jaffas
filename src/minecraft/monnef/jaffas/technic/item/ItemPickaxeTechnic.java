package monnef.jaffas.technic.item;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;

public class ItemPickaxeTechnic extends ItemPickaxe {
    public ItemPickaxeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, material);
        setCreativeTab(JaffasTechnic.CreativeTab);
        //setIconIndex(textureOffset);
        //setTextureFile(JaffasTechnic.textureFile);
    }
}
