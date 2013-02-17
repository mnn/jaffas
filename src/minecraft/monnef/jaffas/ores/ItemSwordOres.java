package monnef.jaffas.ores;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;

public class ItemSwordOres extends ItemSword {
    public ItemSwordOres(int id, int textureOffset, EnumToolMaterial material) {
        super(id, material);
        setCreativeTab(mod_jaffas_ores.CreativeTab);
        setIconIndex(textureOffset);
        setTextureFile(mod_jaffas_ores.textureFile);
    }
}
