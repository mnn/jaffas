/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.DyeHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockMultiLampDummy extends BlockTechnic {
    public BlockMultiLampDummy(int id, int textureID) {
        super(id, textureID, Material.air);
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return BlockMultiLamp.shadeIcon;
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return getRenderColor(world.getBlockMetadata(x, y, z));
    }

    @Override
    public int getRenderColor(int meta) {
        return DyeHelper.getIntColor(meta);
    }
}
