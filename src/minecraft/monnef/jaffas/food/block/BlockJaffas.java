package monnef.jaffas.food.block;

import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockJaffas extends Block {
    public BlockJaffas(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
        setCreativeTab(mod_jaffas_food.CreativeTab);
    }

    @Override
    public String getTextureFile() {
        return mod_jaffas_food.textureFile[getTextureFileIndex()];
    }

    public int getTextureFileIndex() {
        return 0;
    }
}
