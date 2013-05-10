/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

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
}
