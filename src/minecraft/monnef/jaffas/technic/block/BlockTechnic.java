package monnef.jaffas.technic.block;

import monnef.jaffas.food.block.BlockJaffas;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.block.material.Material;

public class BlockTechnic extends BlockJaffas {

    public BlockTechnic(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(JaffasTechnic.CreativeTab);
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
