package monnef.jaffas.food.block;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.food.jaffasFood;
import net.minecraft.block.material.Material;

public abstract class BlockJaffas extends BlockMonnefCore {
    public BlockJaffas(int par1, int par2, Material par3Material) {
        super(par1, par3Material);
        setCreativeTab(jaffasFood.CreativeTab);
        // TODO icon handling
    }

    /*
    @Override
    public String getTextureFile() {
        return jaffasFood.textureFile[getTextureFileIndex()];
    }
    */
}
