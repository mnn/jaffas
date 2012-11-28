package monnef.jaffas.ores;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

public class BlockOres extends Block {

    public BlockOres(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    public String getTextureFile() {
        return mod_jaffas_ores.textureFile;
    }
}
