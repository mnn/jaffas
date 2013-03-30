package monnef.jaffas.trees.block;

import forestry.api.cultivation.ICropEntity;
import monnef.core.utils.BlockHelper;
import monnef.jaffas.trees.block.BlockJaffaCrops;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

import static monnef.core.utils.BlockHelper.*;

public class TileEntityJaffaCrops extends TileEntity implements ICropEntity {

    @Override
    public boolean isHarvestable() {
        BlockJaffaCrops b = (BlockJaffaCrops) this.getBlockType();
        return this.getBlockMetadata() == b.getPhasesMax();
    }

    @Override
    public int[] getNextPosition() {
        return new int[0];
    }

    @Override
    public ArrayList<ItemStack> doHarvest() {
        BlockJaffaCrops b = (BlockJaffaCrops) this.getBlockType();
        ArrayList<ItemStack> res = b.getBlockDropped(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), 0);
        setBlock(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 0);
        return res;
    }
}
