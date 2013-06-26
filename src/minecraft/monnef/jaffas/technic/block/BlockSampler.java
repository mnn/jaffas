/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSampler extends BlockRedstoneCircuit {
    public BlockSampler(int id, int textureStart, int texturesCountPerSet, Material material) {
        super(id, textureStart, texturesCountPerSet, material, TextureMappingType.ALL_SIDES);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntitySampler();
    }
}
