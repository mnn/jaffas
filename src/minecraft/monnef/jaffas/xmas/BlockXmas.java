package monnef.jaffas.xmas;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockXmas extends Block {
    public BlockXmas(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(CreativeTabs.tabBlock);
        setCreativeTab(mod_jaffas_xmas.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_xmas.textureFile;
    }

    public Block setBlockName(String par1Str) {
        return super.setBlockName("jaffas.xmas." + par1Str);
    }
}
