package monnef.jaffas.food.block;

import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class BlockContainerJaffas extends BlockContainer {
    public BlockContainerJaffas(int par1, Material par2Material) {
        super(par1, par2Material);
        init();
    }

    protected void init() {
        setCreativeTab(mod_jaffas_food.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_food.textureFile[getTextureFileIndex()];
    }

    public int getTextureFileIndex() {
        return 0;
    }
}
