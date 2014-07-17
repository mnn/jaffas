package monnef.jaffas.technic.block.redstone;

import monnef.jaffas.food.block.BlockJDirectional;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAnalogRepeater extends BlockRedstoneCircuit {
    public BlockAnalogRepeater(int textureStart, int texturesCountPerSet) {
        super(textureStart, texturesCountPerSet, BlockJDirectional.TextureMappingType.ALL_SIDES);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileAnalogRepeater();
    }
}
