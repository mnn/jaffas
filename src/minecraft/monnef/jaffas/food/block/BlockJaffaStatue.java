package monnef.jaffas.food.block;

import monnef.jaffas.food.jaffasFood;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.*;

public class BlockJaffaStatue extends BlockJaffas {
    public BlockJaffaStatue(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
        //setRequiresSelfNotify();
        setUnlocalizedName("blockJaffaStatue");
        setHardness(1f);
        setResistance(10f);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityJaffaStatue();
    }

    @Override
    public int getRenderType() {
        return jaffasFood.renderID;
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

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving) {
        int rotation = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        setBlockMetadata(par1World, par2, par3, par4, rotation);
    }
}
