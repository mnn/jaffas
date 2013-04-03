package monnef.jaffas.food.block;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.food.jaffasFood;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockJaffas extends BlockMonnefCore {
    public BlockJaffas(int id, int texture, Material material) {
        super(id, texture, material);
        setCreativeTab(jaffasFood.CreativeTab);
    }

    @Override
    public String getCustomIconName() {
        return null;
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 1;
    }

    @Override
    public Block setUnlocalizedName(String par1Str) {
        return super.setUnlocalizedName("jaffas." + par1Str);
    }
}
