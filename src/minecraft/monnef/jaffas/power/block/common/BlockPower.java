package monnef.jaffas.power.block.common;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.block.material.Material;

public class BlockPower extends BlockMonnefCore {
    public BlockPower(int id, int iconIndex, Material material) {
        super(id, iconIndex, material);
        setCreativeTab(mod_jaffas_power.CreativeTab);
    }

    @Override
    public String getTextureFile() {
        return mod_jaffas_power.textureFile;
    }
}
