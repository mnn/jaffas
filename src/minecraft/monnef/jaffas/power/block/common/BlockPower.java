package monnef.jaffas.power.block.common;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.block.material.Material;

public class BlockPower extends BlockMonnefCore {
    public BlockPower(int id, Material material) {
        super(id, material);
        setCreativeTab(mod_jaffas_power.CreativeTab);
    }
}
