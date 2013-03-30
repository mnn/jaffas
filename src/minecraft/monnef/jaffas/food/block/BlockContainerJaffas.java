package monnef.jaffas.food.block;

import monnef.jaffas.food.jaffasFood;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class BlockContainerJaffas extends BlockContainer {
    public BlockContainerJaffas(int par1, Material par2Material) {
        super(par1, par2Material);
        init();
    }

    protected void init() {
        setCreativeTab(jaffasFood.CreativeTab);
    }

    public String getTextureFile() {
        return jaffasFood.textureFile[getTextureFileIndex()];
    }

    public int getTextureFileIndex() {
        return 0;
    }
}
