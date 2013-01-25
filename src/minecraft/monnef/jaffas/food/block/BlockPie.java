package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockPie extends BlockJaffas {
    public static final float f2 = 2F / 16F;
    public static final float f3 = 3F / 16F;
    public static final float f3d = 1F - f3;
    public static final String[] multiBlockNames = new String[]{"Strawberry Pie", "Raspberry Pie", "Vanilla Pie", "Plum Pie"};

    public BlockPie(int par1, int par2) {
        super(par1, par2, Material.cake);
        setBlockName("blockJPie");
        setBlockBounds(f3, 0, f3, f3d, f3, f3d);
        setHardness(0.5f);

        if (TileEntityPie.PieType.values().length != multiBlockNames.length) {
            throw new RuntimeException("pie types number != pie types title number");
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double) ((float) par2 + f3), (double) par3, (double) ((float) par4 + f3), (double) ((float) par2 + f3d), (double) ((float) par3 + f3), (double) ((float) par4 + f3d));
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityPie();
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
        par1World.setBlockTileEntity(par2, par3, par4, createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving) {
        int rotation = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (!par1World.isRemote) {
            TileEntityPie tile = (TileEntityPie) par1World.getBlockTileEntity(par2, par3, par4);
            int meta = par1World.getBlockMetadata(par2, par3, par4);
            tile.init(rotation, meta);
        }
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        TileEntityPie te = (TileEntityPie) par1World.getBlockTileEntity(par2, par3, par4);
        te.eatPiece(par5EntityPlayer);
        return true;
    }

    //from cake block
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        if (!this.canBlockStay(par1World, par2, par3, par4)) {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
    }

    public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
        return par1World.getBlockMaterial(par2, par3 - 1, par4).isSolid();
    }

    public int idDropped(int par1, Random par2Random, int par3) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4) {
        return mod_jaffas.blockPie.blockID;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && canBlockStay(par1World, par2, par3, par4);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
        for (int ix = 0; ix < TileEntityPie.PieType.values().length; ix++) {
            subItems.add(new ItemStack(this, 1, ix));
        }
    }
}
