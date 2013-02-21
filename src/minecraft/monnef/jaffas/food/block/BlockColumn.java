package monnef.jaffas.food.block;

import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockColumn extends BlockJaffas {
    public BlockColumn(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
        setRequiresSelfNotify();
        setBlockName("blockColumn");
        setHardness(1f);
        setResistance(10f);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityColumn();
    }

    @Override
    public int getRenderType() {
        return mod_jaffas_food.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setBlockTileEntity(par2, par3, par4, createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

}
