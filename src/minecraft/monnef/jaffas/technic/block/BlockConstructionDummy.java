/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockConstructionDummy extends BlockTechnic {
    public BlockConstructionDummy(int id, int textureID) {
        super(id, textureID, Material.iron);
        setHardness(5);
        setResistance(15);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityConstructionDummy();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return JaffasFood.renderID;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        TileEntityConstructionDummy dummy = (TileEntityConstructionDummy) world.getBlockTileEntity(x, y, z);

        if (dummy != null && dummy.getCore() != null)
            dummy.getCore().invalidateMultiblock();

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking())
            return false;

        TileEntityConstructionDummy dummy = (TileEntityConstructionDummy) world.getBlockTileEntity(x, y, z);

        if (dummy != null && dummy.getCore() != null) {
            TileEntityCompostCore core = dummy.getCore();
            return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
        }

        return true;
    }
}
