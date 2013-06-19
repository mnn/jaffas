package monnef.jaffas.technic.block;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockKeg extends BlockTechnic {
    private static final float border = 3f * 1f / 16f;
    private static final float borderComplement = 1f - border;

    public BlockKeg(int id, int textureID) {
        super(id, textureID, Material.wood);
        setCreativeTab(null);
        setBlockBounds(border, 0, border, borderComplement, 1, borderComplement);
    }

    public TileEntityKeg getTile(World world, int x, int y, int z) {
        return (TileEntityKeg) world.getBlockTileEntity(x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        return getTile(world, x, y, z).onBlockActivated(player);
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityKeg();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
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
    public int idDropped(int id, Random par2Random, int par3) {
        return JaffasTechnic.itemKeg.itemID;
    }
}
