package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesBaseJaffas extends BlockJaffas {
    public boolean graphicsLevel;

    protected BlockLeavesBaseJaffas(int par1, int texture, Material par2Material, boolean par3) {
        super(par1, texture, par2Material);
        this.graphicsLevel = par3;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        int i1 = par1IBlockAccess.getBlockId(par2, par3, par4);
        return !this.graphicsLevel && i1 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }
}
