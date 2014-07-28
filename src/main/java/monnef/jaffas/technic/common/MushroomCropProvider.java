/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import monnef.jaffas.technic.block.BlockFungiBox;
import monnef.jaffas.technic.block.TileFungiBox;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MushroomCropProvider implements IFarmable {
    @Override
    public boolean isSaplingAt(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (b == null) return false;
        if (!(b instanceof BlockFungiBox)) return false;
        TileFungiBox tile = (TileFungiBox) world.getTileEntity(x, y, z);
        return tile.mushroomPlanted() && !tile.canBeHarvested();
    }

    @Override
    public ICrop getCropAt(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (b == null) return null;
        if (!(b instanceof BlockFungiBox)) return null;
        TileFungiBox tile = (TileFungiBox) world.getTileEntity(x, y, z);
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
        Block b = world.getBlock(x, y, z);
        if (b == null) return false;
        if (!(b instanceof BlockFungiBox)) return false;
        TileFungiBox tile = (TileFungiBox) world.getTileEntity(x, y, z);
        if (tile.tryPlant(germling)) {
            return true;
        } else {
            return false;
        }
    }
}
