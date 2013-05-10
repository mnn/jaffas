/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.common;

import forestry.api.cultivation.ICropEntity;
import forestry.api.cultivation.ICropProvider;
import monnef.jaffas.trees.block.BlockJaffaCrops;
import monnef.jaffas.trees.block.TileEntityJaffaCrops;
import monnef.jaffas.trees.item.ItemJaffaSeeds;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

// adds forestry compatibility
public class JaffaCropProvider implements ICropProvider {
    @Override
    public boolean isGermling(ItemStack germling) {
        if (germling == null) return false;

        if (germling.getItem() instanceof ItemJaffaSeeds)
            return true;

        return false;
    }

    @Override
    public boolean isCrop(World world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        Block block = Block.blocksList[id];

        if (block instanceof BlockJaffaCrops) return true;

        return false;
    }

    @Override
    public ItemStack[] getWindfall() {
        return new ItemStack[0];
    }

    private static int topSide = 1;

    @Override
    public boolean doPlant(ItemStack germling, World world, int x, int y, int z) {
        if (germling == null) return false;
        ItemJaffaSeeds seeds = (ItemJaffaSeeds) germling.getItem();
        //boolean placed = seeds.tryPlaceIntoWorld(germling, null, world, x, y - 1, z, topSide, 0, 0, 0);
        boolean placed = seeds.onItemUse(germling, null, world, x, y - 1, z, topSide, 0, 0, 0);
        if (placed) germling.stackSize++;
        return placed;
    }

    @Override
    public ICropEntity getCrop(World world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        Block block = Block.blocksList[id];

        if (block instanceof BlockJaffaCrops) {
            TileEntity te = world.getBlockTileEntity(x, y, z);
            if (te != null && te instanceof TileEntityJaffaCrops) return (TileEntityJaffaCrops) te;
        }

        return null;
    }
}
