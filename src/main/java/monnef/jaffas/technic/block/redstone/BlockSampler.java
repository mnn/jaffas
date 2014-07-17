/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSampler extends BlockRedstoneCircuit {
    public BlockSampler(int textureStart, int texturesCountPerSet) {
        super(textureStart, texturesCountPerSet, TextureMappingType.ALL_SIDES);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileSampler();
    }
}
