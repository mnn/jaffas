package monnef.jaffas.food.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRipeningBox extends BlockContainerJaffas {
    public BlockRipeningBox(int id, int index, Material material) {
        super(id, index, material);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileRipeningBox();
    }
}
