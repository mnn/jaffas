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
    public String customIconName() {
        return "todo";
    }

    @Override
    public String getModId() {
        return Reference.ModId;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 1;
    }
}
