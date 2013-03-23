package monnef.jaffas.xmas;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;

public class BlockXmasMulti extends BlockXmas {
    private final int subBlocksCount;

    public BlockXmasMulti(int id, int textureID, Material material, int subBlocksCount) {
        super(id, textureID, material);
        this.subBlocksCount = subBlocksCount;
    }

    @Override
    public Icon getBlockTextureFromSideAndMetadata(int side, int metadata) {
        return null;
        //return this.blockIndexInTexture + metadata;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
        for (int ix = 0; ix < this.subBlocksCount; ix++) {
            subItems.add(new ItemStack(this, 1, ix));
        }
    }

    /*
    public void registerNames() {
        for (int i = 0; i < subNames.length; i++) {
            //ItemStack multiBlockStack = new ItemStack(this, 1, i);
            //LanguageRegistry.addName(multiBlockStack, subTitles[multiBlockStack.getItemDamage()]);
            LanguageRegistry.instance().addStringLocalization(this.getBlockName() + "." + subNames[i] + ".name", subTitles[i]);
        }
    }  */
}
