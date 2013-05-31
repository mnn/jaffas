/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import forestry.api.cultivation.ICropEntity;
import forestry.api.cultivation.ICropProvider;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.TileEntityFungiBox;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MushroomCropProvider implements ICropProvider {
    @Override
    public boolean isGermling(ItemStack germling) {
        return false;
    }

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
}
