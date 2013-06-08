/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.base.CustomIconHelper;
import monnef.core.utils.BlockHelper;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMultiLamp extends BlockTechnic {
    public static Icon shadeIcon;
    private int shadeTextureID;

    public BlockMultiLamp(int id, int textureID, int shadeTextureID) {
        super(id, textureID, Material.iron);
        this.shadeTextureID = shadeTextureID;
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
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neightbourBlockId) {
        super.onNeighborBlockChange(world, x, y, z, neightbourBlockId);
        onChange(world, x, y, z);
    }

    private void onChange(World world, int x, int y, int z) {
        if (world.isRemote) return;
        int myMeta = world.getBlockMetadata(x, y, z);
        int power = world.getStrongestIndirectPower(x, y, z);
        //JaffasFood.Log.printDebug(String.format("myMeta=%d, power=%d, strIndPower=%d, inputPower=%d", myMeta, power, world.getStrongestIndirectPower(x, y, z), world.getBlockPowerInput(x, y, z)));
        if (power != myMeta) {
            BlockHelper.setBlockMetadata(world, x, y, z, power);
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == 0 ? 0 : 15;
    }
}
