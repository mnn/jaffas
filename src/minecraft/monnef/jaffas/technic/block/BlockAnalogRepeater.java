package monnef.jaffas.technic.block;

import net.minecraft.block.material.Material;

public class BlockAnalogRepeater extends BlockRedstoneCircuit {
    public BlockAnalogRepeater(int id, int textureStart, int texturesCountPerSet, Material material) {
        super(id, textureStart, texturesCountPerSet, material, TextureMappingType.ALL_SIDES);
    }
}
