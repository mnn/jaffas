/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.food.block.BlockJDirectional;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.block.material.Material;

public class BlockDirectionalTechnic extends BlockJDirectional {
    public BlockDirectionalTechnic(int id, int textureStart, int texturesCountPerSet, Material material, TextureMappingType type) {
        this(id, textureStart, texturesCountPerSet, material, type, 1);
    }

    public BlockDirectionalTechnic(int id, int textureStart, int texturesCountPerSet, Material material, TextureMappingType type, int textureSetsCount) {
        super(id, textureStart, texturesCountPerSet, material, type, textureSetsCount);
        setCreativeTab(JaffasTechnic.instance.creativeTab);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 3;
    }
}
