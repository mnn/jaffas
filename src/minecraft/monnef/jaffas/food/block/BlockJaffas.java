package monnef.jaffas.food.block;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.food.Reference;
import monnef.jaffas.food.jaffasFood;
import net.minecraft.block.material.Material;

public abstract class BlockJaffas extends BlockMonnefCore {
    public BlockJaffas(int id, int index, Material material) {
        super(id, index, material);
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
}
