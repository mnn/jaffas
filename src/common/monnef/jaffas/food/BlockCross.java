package monnef.jaffas.food;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockCross extends Block {

    public BlockCross(int id, int texture, Material material) {
        super(id, texture, material);
        setRequiresSelfNotify();
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityCross();
    }

    @Override
    public int getRenderType() {
        return mod_jaffas.renderID;
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
    public String getTextureFile() {
        return "/jaffas_01.png";
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
