/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import forestry.api.farming.ICrop;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collection;

import static monnef.core.utils.BlockHelper.setAir;

/**
 * Forestry integration
 */
public class TileJaffaCrops extends TileEntity implements ICrop {
    @Override
    public Collection<ItemStack> harvest() {
        BlockJaffaCrops b = (BlockJaffaCrops) this.getBlockType();
        ArrayList<ItemStack> res = b.getDrops(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), 0);
        setAir(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        return res;
    }
}
