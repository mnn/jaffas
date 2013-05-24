/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;

public class BlockConstruction extends BlockTechnic {
    public BlockConstruction(int id, int textureID) {
        super(id, textureID, Material.iron);
        setIconsCount(2);
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return icons[meta];
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, 0));
        par3List.add(new ItemStack(this, 1, 1));
    }
}
