package monnef.core.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockMonnefCore extends Block {
    public BlockMonnefCore(int id, Material material) {
        super(id, material);
    }

    public BlockMonnefCore(int id, int iconIndex, Material material) {
        super(id, iconIndex, material);
    }

    @Override
    public abstract String getTextureFile();
}
