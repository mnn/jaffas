package monnef.jaffas.food.block;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.food.Reference;
import monnef.jaffas.food.jaffasFood;
import net.minecraft.block.material.Material;

public abstract class BlockJaffas extends BlockMonnefCore {
    public BlockJaffas(int par1, int par2, Material par3Material) {
        super(par1, par3Material);
        setCreativeTab(jaffasFood.CreativeTab);
    }

    @Override
    public String getCustomIconName() {
        return "todo";
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 1;
    }
}
