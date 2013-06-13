/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.base.CustomIconHelper;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.RandomHelper;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.client.EntityLampLightFX;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMultiLamp extends BlockTechnic {
    public static Icon shadeIcon;
    private int shadeTextureID;

    public BlockMultiLamp(int id, int textureID, int shadeTextureID) {
        super(id, textureID, Material.rock);
        this.shadeTextureID = shadeTextureID;
        setHardness(1f);
        setResistance(1);
        stepSound = soundGlassFootstep;
        setLightValue(0);
    }

    @Override
    public int getRenderType() {
        return JaffasTechnic.lampRenderID;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
        shadeIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this, shadeTextureID));
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        onChange(world, x, y, z);
        refreshMetadata(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neightbourBlockId) {
        super.onNeighborBlockChange(world, x, y, z, neightbourBlockId);
        onChange(world, x, y, z);
    }

    private void onChange(World world, int x, int y, int z) {
        if (world.isRemote) return;
        int myMeta = world.getBlockMetadata(x, y, z);
        int power = getPower(world, x, y, z);
        //JaffasFood.Log.printDebug(String.format("myMeta=%d, power=%d, strIndPower=%d, inputPower=%d", myMeta, power, world.getStrongestIndirectPower(x, y, z), world.getBlockPowerInput(x, y, z)));
        if (power != myMeta) {
            if (myMeta != 0) {
                //world.scheduleBlockUpdate(x, y, z, this.blockID, 4);
                refreshMetadata(world, x, y, z);
            } else {
                refreshMetadata(world, x, y, z);
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x, y, z);
        if (!world.isRemote && meta != 0 && !(getPower(world, x, y, z) > 0)) {
            refreshMetadata(world, x, y, z);
        }
    }

    private int getPower(World world, int x, int y, int z) {
        return world.getStrongestIndirectPower(x, y, z);
    }

    private void refreshMetadata(World world, int x, int y, int z) {
        int power = getPower(world, x, y, z);
        int bId = world.getBlockId(x, y, z);
        if (bId == blockID) {
            BlockHelper.setBlockMetadata(world, x, y, z, power);
            // some magic is happening down here...
            world.markBlockForUpdate(x, y, z);
            world.updateAllLightTypes(x, y, z);
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        int currBlockId = world.getBlockId(x, y, z);
        if (currBlockId != blockID) {
            // not lamp
            return 0;
            //return super.getLightValue(world, x, y, z);
        }
        return meta == 0 ? 0 : 15;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0 || JaffasTechnic.disableLampParticles) return;

        float speed = 0.5f;
        float mx = RandomHelper.generateRandomFromSymmetricInterval(speed);
        float my = RandomHelper.generateRandomFromSymmetricInterval(speed);
        float mz = RandomHelper.generateRandomFromSymmetricInterval(speed);
        EntityLampLightFX fx = new EntityLampLightFX(world, x + .5, y + .5, z + .5, mx, my, mz, 30);
        BlockMultiLampDummy.configureColor(fx, meta);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
    }
}
