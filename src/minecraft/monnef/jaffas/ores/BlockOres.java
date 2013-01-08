package monnef.jaffas.ores;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOres extends Block {

    public BlockOres(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(CreativeTabs.tabBlock);
        setCreativeTab(mod_jaffas_ores.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_ores.textureFile;
    }
}
