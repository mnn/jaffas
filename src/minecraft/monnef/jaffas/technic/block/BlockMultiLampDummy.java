/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.ColorHelper;
import monnef.core.utils.DyeHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockMultiLampDummy extends BlockTechnic {
    private static int colors[];

    static {
        colors = new int[16];
        for (int i = 0; i < 16; i++) {
            int c = DyeHelper.getIntColor(i);
            if (i > 0 && i <= 9) c = ColorHelper.addContrast(c, 1.33f);
            colors[i] = c;
        }
    }

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
        return colors[meta];
    }
}
