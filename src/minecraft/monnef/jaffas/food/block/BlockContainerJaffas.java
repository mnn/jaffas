package monnef.jaffas.food.block;

import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class BlockContainerJaffas extends BlockContainer {
    public BlockContainerJaffas(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
        init();
    }

    public BlockContainerJaffas(int par1, Material par2Material) {
        super(par1, par2Material);
        init();
    }

    protected void init() {
        setCreativeTab(mod_jaffas.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas.textureFile;
    }
}
