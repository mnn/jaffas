package monnef.jaffas.technic.block;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.block.material.Material;

public class BlockTechnic extends BlockMonnefCore {

    public BlockTechnic(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(JaffasTechnic.CreativeTab);
    }

    public String getTextureFile() {
        return JaffasTechnic.textureFile;
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
