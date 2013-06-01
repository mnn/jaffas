/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import monnef.jaffas.technic.block.BlockFungiBox;
import monnef.jaffas.technic.block.TileEntityFungiBox;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MushroomCropProvider implements IFarmable {
    @Override
    public boolean isSaplingAt(World world, int x, int y, int z) {
        Block b = Block.blocksList[world.getBlockId(x, y, z)];
        if (b == null) return false;
        if (!(b instanceof BlockFungiBox)) return false;
        TileEntityFungiBox tile = (TileEntityFungiBox) world.getBlockTileEntity(x, y, z);
        return tile.mushroomPlanted() && !tile.canBeHarvested();
    }

    @Override
    public ICrop getCropAt(World world, int x, int y, int z) {
        Block b = Block.blocksList[world.getBlockId(x, y, z)];
        if (b == null) return null;
        if (!(b instanceof BlockFungiBox)) return null;
        TileEntityFungiBox tile = (TileEntityFungiBox) world.getBlockTileEntity(x, y, z);
        if (!tile.mushroomPlanted() || !tile.canBeHarvested()) return null;
        return tile;
    }

    @Override
    public boolean isGermling(ItemStack germling) {
        if (germling == null) return false;
        return FungiCatalog.findByDrop(germling) != null;
    }

    @Override
    public boolean isWindfall(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean plantSaplingAt(ItemStack germling, World world, int x, int y, int z) {
        Block b = Block.blocksList[world.getBlockId(x, y, z)];
        if (b == null) return false;
        if (!(b instanceof BlockFungiBox)) return false;
        TileEntityFungiBox tile = (TileEntityFungiBox) world.getBlockTileEntity(x, y, z);
        if (tile.tryPlant(germling)) {
            return true;
        } else {
            return false;
        }
    }

    /*
    @Override
    public boolean isCrop(World world, int x, int y, int z) {
        return world.getBlockId(x, y, z) == JaffasTechnic.fungiBox.blockID;
    }

    @Override
    public ItemStack[] getWindfall() {
        return new ItemStack[]{};
    }

    @Override
    public boolean doPlant(ItemStack germling, World world, int x, int y, int z) {
        return false;
    }

    @Override
    public ICropEntity getCrop(World world, int x, int y, int z) {
        return (TileEntityFungiBox) world.getBlockTileEntity(x, y, z);
    }
    */
}
