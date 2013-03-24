package monnef.jaffas.technic.block;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.technic.mod_jaffas_technic;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockTechnic extends BlockMonnefCore {

    public BlockTechnic(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(mod_jaffas_technic.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_technic.textureFile;
    }
}
