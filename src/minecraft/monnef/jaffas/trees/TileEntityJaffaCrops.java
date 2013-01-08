package monnef.jaffas.trees;

import forestry.api.cultivation.ICropEntity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

import java.util.ArrayList;

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
        this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, 0);
        return res;
    }
}
