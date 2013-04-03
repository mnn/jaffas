package monnef.jaffas.power.block.common;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.power.common.Reference;
import monnef.jaffas.power.jaffasPower;
import net.minecraft.block.material.Material;

public class BlockPower extends BlockMonnefCore {
    public BlockPower(int id, Material material) {
        super(id, material);
        init();
    }

    public BlockPower(int id, int index, Material material) {
        super(id, index, material);
        init();
    }

    private void init() {
        setCreativeTab(jaffasPower.CreativeTab);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 5;
    }
}
