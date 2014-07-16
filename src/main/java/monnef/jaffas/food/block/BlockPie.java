/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.common.CustomIconHelper;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import static monnef.core.utils.BlockHelper.setAir;
import static monnef.core.utils.BlockHelper.setBlock;

public class BlockPie extends BlockJaffas {
    public static final float f2 = 2F / 16F;
    public static final float f3 = 3F / 16F;
    public static final float f3d = 1F - f3;
    public static final String[] multiBlockNames = new String[]{"Strawberry Pie", "Raspberry Pie", "Vanilla Pie", "Plum Pie"};
    public static final int[] textureIndexFromMeta = new int[]{156, 157, 159, 158};
    public static IIcon[] icons;

    public BlockPie(int texture) {
        super(texture, Material.cake);
        setBlockName("blockJPie");
        setBlockBounds(f3, 0, f3, f3d, f3, f3d);
        setHardness(0.5f);

        if (TilePie.PieType.values().length != multiBlockNames.length) {
            throw new RuntimeException("pie types number != pie types title number");
        }

        if (TilePie.PieType.values().length != textureIndexFromMeta.length) {
            throw new RuntimeException("pie types number != texture types title number");
        }
        icons = new IIcon[textureIndexFromMeta.length];
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return AxisAlignedBB.getAABBPool().getAABB((double) ((float) par2 + f3), (double) par3, (double) ((float) par4 + f3), (double) ((float) par2 + f3d), (double) ((float) par3 + f3), (double) ((float) par4 + f3d));
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TilePie();
    }

    @Override
    public int getRenderType() {
        return ContentHolder.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setTileEntity(par2, par3, par4, createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        int rotation = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (!world.isRemote) {
            TilePie tile = (TilePie) world.getTileEntity(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            tile.init(rotation, meta);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        TilePie te = (TilePie) world.getTileEntity(x, y, z);
        te.eatPiece(player);
        return true;
    }

    //from cake block
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            setAir(world, x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z).getMaterial().isSolid();
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4) {
        return ContentHolder.blockPie.blockID;
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
    public void getSubBlocks(Item item, CreativeTabs tabs, List result) {
        for (int ix = 0; ix < TilePie.PieType.values().length; ix++) {
            result.add(new ItemStack(this, 1, ix));
        }
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return icons[metadata];
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        for (int i = 0; i < textureIndexFromMeta.length; i++) {
            icons[i] = iconRegister.registerIcon(CustomIconHelper.generateId(this, textureIndexFromMeta[i]));
        }
    }
}
