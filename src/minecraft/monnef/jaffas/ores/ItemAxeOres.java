package monnef.jaffas.ores;

import net.minecraft.item.*;

public class ItemAxeOres extends ItemAxe {
    public ItemAxeOres(int id, int textureOffset, EnumToolMaterial material) {
        super(id, material);
        setCreativeTab(mod_jaffas_ores.CreativeTab);
        setIconIndex(textureOffset);
        setTextureFile(mod_jaffas_ores.textureFile);
    }
}
