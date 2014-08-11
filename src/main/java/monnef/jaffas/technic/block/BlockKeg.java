package monnef.jaffas.technic.block;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.xmas.JaffasXmas;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Random;

public class BlockKeg extends BlockTechnic {
    private static final float border = 3f * 1f / 16f;
    private static final float borderComplement = 1f - border;

    public BlockKeg(int textureID) {
        super(textureID, Material.wood);
        removeFromCreativeTab();
        setBlockBounds(border, 0, border, borderComplement, 1, borderComplement);
    }

    public TileKeg getTile(World world, int x, int y, int z) {
        return (TileKeg) world.getTileEntity(x, y, z);
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
        return new TileKeg();
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
        return ContentHolder.renderID;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int par3) {
        return JaffasTechnic.itemKeg;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        TileKeg.KegType liquidType = ((TileKeg) world.getTileEntity(x, y, z)).getLiquidType();
        int dmg = liquidType != null ? liquidType.ordinal() : 0;
        return new ItemStack(JaffasTechnic.itemKeg, 1, dmg);
    }
}
