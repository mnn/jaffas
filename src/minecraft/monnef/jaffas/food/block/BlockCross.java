package monnef.jaffas.food.block;

import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCross extends BlockJaffas {

    public BlockCross(int id, int texture, Material material) {
        super(id, texture, material);
        setCreativeTab(null);
        //setRequiresSelfNotify();
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityCross();
    }

    @Override
    public int getRenderType() {
        return JaffasFood.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setBlockTileEntity(par2, par3, par4, this.createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
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
