/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.Random;

public abstract class BlockRedstoneCircuit extends BlockDirectionalTechnic {
    public static final int WAIT_TICKS = 2;

    public BlockRedstoneCircuit(int id, int textureStart, int texturesCountPerSet, Material material, TextureMappingType type) {
        super(id, textureStart, texturesCountPerSet, material, type);
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
    public abstract TileEntity createTileEntity(World world, int metadata);

    public TileEntityRedstoneCircuit getTile(IBlockAccess world, int x, int y, int z) {
        return (TileEntityRedstoneCircuit) world.getBlockTileEntity(x, y, z);
    }

    public void recalculatePower(IBlockAccess world, int x, int y, int z) {
        getTile(world, x, y, z).recalculatePower();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourBlockID) {
        super.onNeighborBlockChange(world, x, y, z, neighbourBlockID);
        recalculatePowerInNextTick(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        recalculatePowerInNextTick(world, x, y, z);
    }

    private void recalculatePowerInNextTick(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, blockID, WAIT_TICKS);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
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
