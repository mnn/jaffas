/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

import monnef.jaffas.food.block.BlockJaffas;
import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.common.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockXmas extends BlockJaffas {
    public BlockXmas(int id, int textureID, Material material) {
        super(id, textureID, material);
        setCreativeTab(CreativeTabs.tabBlock);
        setCreativeTab(JaffasXmas.instance.creativeTab);
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
