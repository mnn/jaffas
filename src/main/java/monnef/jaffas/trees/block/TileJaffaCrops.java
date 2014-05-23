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

import static monnef.core.utils.BlockHelper.setBlock;

public class TileJaffaCrops extends TileEntity implements ICrop {
    @Override
    public Collection<ItemStack> harvest() {
        BlockJaffaCrops b = (BlockJaffaCrops) this.getBlockType();
        ArrayList<ItemStack> res = b.getBlockDropped(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), 0);
        setBlock(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 0);
        return res;
    }
}
