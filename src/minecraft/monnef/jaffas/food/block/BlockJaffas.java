package monnef.jaffas.food.block;

import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockJaffas extends Block {
    public BlockJaffas(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
        setCreativeTab(mod_jaffas.CreativeTab);
    }

    @Override
    public String getTextureFile() {
        return mod_jaffas.textureFile[getTextureFileIndex()];
    }

    public int getTextureFileIndex() {
        return 0;
    }
}
