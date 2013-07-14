/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.base.CustomIconHelper;
import monnef.core.utils.DyeHelper;
import monnef.core.utils.RandomHelper;
import monnef.core.utils.StringsHelper;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.client.EntityLampLightFX;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockLamp extends BlockTechnic {
    public static Icon shadeIcon;
    private int shadeTextureID;

    public BlockLamp(int id, int textureID) {
        super(id, textureID, Material.rock);
        setHardness(1f);
        setResistance(1);
        stepSound = soundGlassFootstep;
        setLightValue(1);
        setUnlocalizedName("decoLamp");
    }

    public BlockLamp(int id, int textureID, int shadeTextureID) {
        this(id, textureID);
        this.shadeTextureID = shadeTextureID;
    }

    @Override
    public int getRenderType() {
        return JaffasTechnic.lampRenderID;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
        if (getClass() == BlockLamp.class) {
            shadeIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this, shadeTextureID));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x, y, z);
        if (!getShowParticles(world, x, y, z, meta) || JaffasTechnic.disableLampParticles) return;

        float speed = 0.5f;
        float mx = RandomHelper.generateRandomFromSymmetricInterval(speed);
        float my = RandomHelper.generateRandomFromSymmetricInterval(speed);
        float mz = RandomHelper.generateRandomFromSymmetricInterval(speed);
        EntityLampLightFX fx = new EntityLampLightFX(world, x + .5, y + .5, z + .5, mx, my, mz, 30);
        BlockLampDummy.configureColor(fx, meta);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
    }

    protected boolean getShowParticles(World world, int x, int y, int z, int meta) {
        return true;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        if (blockID == JaffasTechnic.lampDeco.blockID) {
            for (int i = 0; i < 16; i++) {
                par3List.add(new ItemStack(blockID, 1, i));
            }
        } else {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    public boolean shouldForceInventoryColoring() {
        return true;
    }

    public String[] generateTitles() {
        String[] ret = new String[16];
        for (int i = 0; i < 16; i++) {
            ret[i] = StringsHelper.insertSpaceOnLowerUpperCaseChange(StringsHelper.makeFirstCapital(DyeHelper.getDyeColorName(i))) + " Decorative Lamp";
        }
        return ret;
    }

    public String[] generateSubNames() {
        String[] ret = new String[16];
        for (int i = 0; i < 16; i++) {
            ret[i] = "decoLamp" + DyeHelper.getDyeColorName(i);
        }
        return ret;
    }
}
