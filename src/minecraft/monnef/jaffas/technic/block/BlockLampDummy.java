/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.DyeHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.client.EntityLampLightFX;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockLampDummy extends BlockTechnic {
    private static int colors[];
    private static float particleColors[][];

    static {
        colors = new int[16];

        for (int i = 0; i < 16; i++) {
            int c = DyeHelper.getIntColor(i);
            if (i > 0 && i <= 9) c = ColorHelper.addContrast(c, 1.33f);
            colors[i] = c;
        }

        particleColors = new float[16][3];
        for (int i = 0; i < 16; i++) {
            ColorHelper.IntColor c = ColorHelper.getColor(colors[i]);
            particleColors[i][0] = c.getFloatRed();
            particleColors[i][1] = c.getFloatGreen();
            particleColors[i][2] = c.getFloatBlue();
        }

        if (MonnefCorePlugin.debugEnv) {
            JaffasFood.Log.printFinest("Dumping lamp colors:");
            for (int i = 0; i < colors.length; i++) {
                JaffasFood.Log.printFinest(String.format("%d - %s", i, ColorHelper.getColor(colors[i])));
            }
        }
    }

    public BlockLampDummy(int id, int textureID) {
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

    public static void configureColor(EntityLampLightFX light, int meta) {
        float[] color = particleColors[meta];
        light.configureColor(color[0], color[1], color[2]);
    }
}
