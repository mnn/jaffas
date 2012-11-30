package monnef.jaffas.xmas;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

public class BlockXmas extends Block {
    public BlockXmas(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(CreativeTabs.tabBlock);
        setCreativeTab(mod_jaffas_xmas.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_xmas.textureFile;
    }
}
