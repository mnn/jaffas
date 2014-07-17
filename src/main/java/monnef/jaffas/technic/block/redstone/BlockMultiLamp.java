/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.technic.block.BlockLamp;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMultiLamp extends BlockLamp {

    public BlockMultiLamp(int textureID) {
        super(textureID);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        onChange(world, x, y, z);
        refreshMetadata(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neightbourBlockId) {
        super.onNeighborBlockChange(world, x, y, z, neightbourBlockId);
        onChange(world, x, y, z);
    }

    protected void onChange(World world, int x, int y, int z) {
        if (world.isRemote) return;
        int myMeta = world.getBlockMetadata(x, y, z);
        int power = getPower(world, x, y, z);
        //JaffasFood.Log.printDebug(String.format("myMeta=%d, power=%d, strIndPower=%d, inputPower=%d", myMeta, power, world.getStrongestIndirectPower(x, y, z), world.getBlockPowerInput(x, y, z)));
        if (power != myMeta) {
            if (myMeta != 0) {
                //world.scheduleBlockUpdate(x, y, z, this.blockID, 4);
                refreshMetadata(world, x, y, z);
            } else {
                refreshMetadata(world, x, y, z);
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x, y, z);
        if (!world.isRemote && meta != 0 && !(getPower(world, x, y, z) > 0)) {
            refreshMetadata(world, x, y, z);
        }
    }

    protected int getPower(World world, int x, int y, int z) {
        return world.getStrongestIndirectPower(x, y, z);
    }

    protected void refreshMetadata(World world, int x, int y, int z) {
        int power = getPower(world, x, y, z);
        Block block = world.getBlock(x, y, z);
        if (block == this) {
            BlockHelper.setBlockMetadata(world, x, y, z, power);
            // some magic is happening down here...
            world.markBlockForUpdate(x, y, z);
            world.func_147451_t(x, y, z); // updateAllLightTypes
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        Block currBlock = world.getBlock(x, y, z);
        if (currBlock != this) {
            // not lamp
            return 0;
            //return super.getLightValue(world, x, y, z);
        }
        return meta == 0 ? 0 : 15;
    }

    @Override
    public boolean shouldForceInventoryColoring() {
        return false;
    }

    @Override
    protected boolean getShowParticles(World world, int x, int y, int z, int meta) {
        return meta != 0;
    }
}
