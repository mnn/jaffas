package monnef.jaffas.technic.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockSampler extends BlockDirectionalTechnic {
    public BlockSampler(int id, int textureStart, int texturesCountPerSet, Material material) {
        super(id, textureStart, texturesCountPerSet, material, TextureMappingType.ALL_SIDES);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        return getTile(world, x, y, z).getCurrentPowerFromSide(side);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntitySampler();
    }

    public TileEntitySampler getTile(IBlockAccess world, int x, int y, int z) {
        return (TileEntitySampler) world.getBlockTileEntity(x, y, z);
    }

    public void recalculatePower(IBlockAccess world, int x, int y, int z) {
        getTile(world, x, y, z).recalculatePower();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourBlockID) {
        super.onNeighborBlockChange(world, x, y, z, neighbourBlockID);
        recalculatePower(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        recalculatePower(world, x, y, z);
    }

    @Override
    protected int processSideToIconIndexMapping(int side) {
        if (side == ForgeDirection.UP.ordinal()) return 0;
        if (side == ForgeDirection.DOWN.ordinal()) return 1;
        return 2;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int meta) {
        getTile(world, x, y, z).forceUpdateNeighbours();
        super.breakBlock(world, x, y, z, par5, meta);
    }
}
