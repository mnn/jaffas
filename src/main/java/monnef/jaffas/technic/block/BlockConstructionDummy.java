/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockConstructionDummy extends BlockTechnic {
    public BlockConstructionDummy(int textureID) {
        super(textureID, Material.iron);
        setHardness(5);
        setResistance(15);
        setCreativeTab(null);
        removeFromCreativeTab();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileConstructionDummy();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ContentHolder.renderID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        TileConstructionDummy dummy = (TileConstructionDummy) world.getTileEntity(x, y, z);

        if (dummy != null && dummy.getCore() != null)
            dummy.getCore().invalidateMultiblock();

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking())
            return false;

        TileConstructionDummy dummy = (TileConstructionDummy) world.getTileEntity(x, y, z);

        if (dummy != null && dummy.getCore() != null) {
            TileCompostCore core = dummy.getCore();
            return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
        }

        return true;
    }
}
