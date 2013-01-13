package monnef.jaffas.food.block;

import monnef.core.BitHelper;
import monnef.core.PlayerHelper;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSink extends Block {
    public static final int waterBit = 2;
    private static final boolean debug = false;

    public BlockSink(int id, int texture) {
        super(id, texture, Material.iron);
        setRequiresSelfNotify();
        setCreativeTab(null);
        setHardness(2f);
        setResistance(20f);
        //setTickRandomly(true);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntitySink();
    }

    @Override
    public int getRenderType() {
        return mod_jaffas.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setBlockTileEntity(par2, par3, par4, this.createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
    }

    @Override
    public String getTextureFile() {
        return "/jaffas_01.png";
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && super.canPlaceBlockAt(par1World, par2, par3 + 1, par4);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        if (par1World.getBlockId(par2, par3 + 1, par4) != 0) {
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            if (!par1World.isRemote) {
                this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (debug && mod_jaffas.debug && !par1World.isRemote) {
            int m = par1World.getBlockMetadata(par2, par3, par4);
            par5EntityPlayer.sendChatToPlayer("meta: " + m);
        }

        if (par5EntityPlayer.isSneaking()) {
            return false;
        }

        int meta = par1World.getBlockMetadata(par2, par3, par4);
        if (!WaterIsReady(meta)) {
            return false;
        }

        ItemStack currentItem = par5EntityPlayer.getCurrentEquippedItem();
        if (currentItem != null && currentItem.itemID == Item.bucketEmpty.shiftedIndex) {
            par1World.setBlockMetadata(par2, par3, par4, BitHelper.unsetBit(meta, waterBit));
            currentItem.stackSize--;
            if (!par1World.isRemote) {
                PlayerHelper.giveItemToPlayer(par5EntityPlayer, new ItemStack(Item.bucketWater));
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean WaterIsReady(int meta) {
        return BitHelper.isBitSet(meta, waterBit);
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return mod_jaffas.getItem(JaffaItem.sink).shiftedIndex;
    }

    @Override
    public int idPicked(World par1World, int par2, int par3, int par4) {
        return mod_jaffas.getItem(JaffaItem.sink).shiftedIndex;
    }
}
