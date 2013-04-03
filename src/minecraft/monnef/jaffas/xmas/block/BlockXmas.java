package monnef.jaffas.xmas.block;

import monnef.core.base.BlockMonnefCore;
import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.common.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockXmas extends BlockMonnefCore {
    public BlockXmas(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(CreativeTabs.tabBlock);
        setCreativeTab(JaffasXmas.CreativeTab);
    }

    public String getTextureFile() {
        return JaffasXmas.textureFile;
    }

    public Block setUnlocalizedName(String par1Str) {
        return super.setUnlocalizedName("jaffas.xmas." + par1Str);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 4;
    }
}
