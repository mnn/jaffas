/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;

public class BlockConstruction extends BlockTechnic {
    public BlockConstruction(int id, int textureID) {
        super(id, textureID, Material.iron);
        setIconsCount(2);
        setHardness(5);
        setResistance(15);
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

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getLightOpacity(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return isGlassy(meta) ? 1 : 255;
    }

    public static boolean isGlassy(int meta) {
        return meta == 1;
    }

    @Override
    public int getRenderType() {
        return JaffasFood.renderBlockID;
    }
}
