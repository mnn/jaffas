/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.common;

import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import monnef.jaffas.trees.block.BlockJaffaCrops;
import monnef.jaffas.trees.block.TileJaffaCrops;
import monnef.jaffas.trees.item.ItemJaffaSeeds;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

// Forestry compatibility
public class JaffaCropProvider implements IFarmable {
    @Override
    public boolean isSaplingAt(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof BlockJaffaCrops) {
            BlockJaffaCrops crop = (BlockJaffaCrops) block;
            return crop.canGrow(world.getBlockMetadata(x, y, z));
        }
        return false;
    }

    @Override
    public ICrop getCropAt(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);

        if (block instanceof BlockJaffaCrops) {
            TileEntity te = world.getTileEntity(x, y, z);
            BlockJaffaCrops crop = (BlockJaffaCrops) block;
            if (crop.canGrow(world.getBlockMetadata(x, y, z))) return null;
            if (te != null && te instanceof TileJaffaCrops) return (TileJaffaCrops) te;
        }

        return null;
    }

    @Override
    public boolean isGermling(ItemStack germling) {
        if (germling == null) return false;

        if (germling.getItem() instanceof ItemJaffaSeeds)
            return true;

        return false;
    }

    @Override
    public boolean isWindfall(ItemStack itemstack) {
        return false;
    }

    private static int topSide = ForgeDirection.UP.ordinal();

    @Override
    public boolean plantSaplingAt(ItemStack germling, World world, int x, int y, int z) {
        if (germling == null) return false;
        ItemJaffaSeeds seeds = (ItemJaffaSeeds) germling.getItem();
        boolean placed = seeds.onItemUse(germling, null, world, x, y - 1, z, topSide, 0, 0, 0);
        if (placed) germling.stackSize++;
        return placed;
    }
}
