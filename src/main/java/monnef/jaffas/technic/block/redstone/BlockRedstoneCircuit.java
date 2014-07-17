/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import monnef.core.utils.DirectionHelper;
import monnef.jaffas.technic.block.BlockDirectionalTechnic;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public abstract class BlockRedstoneCircuit extends BlockDirectionalTechnic {
    public static final int WAIT_TICKS = 2;
    public static final Material circuitBlock = new Material(MapColor.airColor);

    public BlockRedstoneCircuit(int textureStart, int texturesCountPerSet, TextureMappingType type) {
        super(textureStart, texturesCountPerSet, circuitBlock, type);
        setHardness(0.5f);
        setStepSound(soundTypeStone);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int x, int y, int z, int side) {
        return isProvidingWeakPower(par1IBlockAccess, x, y, z, side);
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

    public TileRedstoneCircuit getTile(IBlockAccess world, int x, int y, int z) {
        return (TileRedstoneCircuit) world.getTileEntity(x, y, z);
    }

    public void recalculatePower(World world, int x, int y, int z) {
        if (getTile(world, x, y, z).recalculatePower()) {
            scheduleNeighboursUpdate(world, x, y, z);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbour) {
        super.onNeighborBlockChange(world, x, y, z, neighbour);
        recalculatePower(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        recalculatePower(world, x, y, z);
    }

    private void scheduleNeighboursUpdate(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, this, WAIT_TICKS);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
        //recalculatePower(world, x, y, z);
        TileRedstoneCircuit tile = getTile(world, x, y, z);
        tile.notifyBlocksOfMyChange();
        tile.notifyOutputNeighbour();
        //tile.notifyOutputNeighbourTwo();
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        getTile(world, x, y, z).forceUpdateNeighbours();
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return getTile(world, x, y, z).canConnectRedstone(DirectionHelper.translateFromRedstoneToClassicSideRepresentation(side));
    }
}
