/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockConstruction extends BlockTechnic {
    public static final int META_ALLOY = 0;
    public static final int META_GLASSY = 1;

    public BlockConstruction(int textureID) {
        super(textureID, Material.iron);
        setIconsCount(2);
        setHardness(5);
        setResistance(15);
        setStepSound(soundTypeMetal);
        setBlockName("constructionBlock");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return getCustomIcon(meta);
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, META_ALLOY));
        par3List.add(new ItemStack(this, 1, META_GLASSY));
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
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return isGlassy(meta) ? 0 : 255;
    }

    public static boolean isGlassy(int meta) {
        return meta == META_GLASSY;
    }

    @Override
    public int getRenderType() {
        return ContentHolder.renderBlockID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        Block neighbour = access.getBlock(x, y, z);
        if (neighbour != this) return super.shouldSideBeRendered(access, x, y, z, side);
        int metaNeighbour = access.getBlockMetadata(x, y, z);
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        int metaMe = access.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
        return !(isGlassy(metaMe) && isGlassy(metaNeighbour));
    }
}
