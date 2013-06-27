package monnef.jaffas.technic.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAnalogRepeater extends BlockRedstoneCircuit {
    public BlockAnalogRepeater(int id, int textureStart, int texturesCountPerSet) {
        super(id, textureStart, texturesCountPerSet, TextureMappingType.ALL_SIDES);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityAnalogRepeater();
    }
}
