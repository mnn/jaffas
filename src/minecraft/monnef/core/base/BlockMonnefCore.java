package monnef.core.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockMonnefCore extends Block {
    public BlockMonnefCore(int id, Material material) {
        super(id, material);
    }

    public BlockMonnefCore(int id, int index, Material material) {
        super(id, material);
        // TODO index
    }
}
