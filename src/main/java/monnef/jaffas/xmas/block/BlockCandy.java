/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

import monnef.jaffas.xmas.JaffasXmas;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

import static monnef.core.utils.BlockHelper.setAir;

public class BlockCandy extends BlockXmas {

    public BlockCandy(int texture, Material material) {
        super(texture, material);
        setHardness(0.5f);
        setCreativeTab(null);
        setBurnProperties(5, 5);
        setBlockName("giantCandyCane");
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileCandy();
    }

    @Override
    public int getRenderType() {
        return JaffasXmas.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        // TODO: necessary?
        world.setTileEntity(x, y, z, this.createTileEntity(world, world.getBlockMetadata(x, y, z)));
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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        int meta = world.getBlockMetadata(x, y, z);

        if (isBlockTopPart(meta)) {
            if (world.getBlock(x, y - 1, z) != this) {
                setAir(world, x, y, z);
            }
        } else {
            boolean destroy = false;
            if (world.getBlock(x, y + 1, z) != this) {
                destroy = true;
            }

            if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
                destroy = true;
            }

            if (destroy) {
                setAir(world, x, y, z);
                if (!world.isRemote) {
                    this.dropBlockAsItem(world, x, y, z, meta, 0);
                }
            }

        }
    }

    public static boolean isBlockTopPart(int meta) {
        return (meta & 8) != 0;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return isBlockTopPart(meta) ? null : JaffasXmas.itemGiantCandy;
    }

    @Override
    public Item getItem(World world, int x, int y, int z) {
        return JaffasXmas.itemGiantCandy;
    }

    public final static float unit = 1f / 16f;
    public final static float f3 = unit * 3;
    public final static float f3d = 1f - unit * 3f;
    public final static float f6 = unit * 6f;
    public final static float f6d = 1f - unit * 6f;
    public final static float f9 = unit * 9f;
    public final static float f9d = 1f - unit * 9f;
    public final static float f4 = unit * 4;
    public final static float f4d = 1f - unit * 4f;
    public final static float f1 = unit * 1;
    public final static float f1d = 1f - unit * 1f;

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int meta = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        BoundsHelper b = new BoundsHelper(meta);

        this.setBlockBounds(b.x1, 0f, b.y1, b.x2, b.top, b.y2);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        BoundsHelper b = new BoundsHelper(meta);

        return AxisAlignedBB.getBoundingBox((double) ((float) par2 + b.x1), (double) par3, (double) ((float) par4 + b.y1), (double) ((float) par2 + b.x2), (double) ((float) par3 + b.top), (double) ((float) par4 + b.y2));
    }

    private class BoundsHelper {
        public float x1, x2, y1, y2;
        public float top = 1f;

        public BoundsHelper(int meta) {

            switch (meta & 3) {
                case 0:
                    x1 = f3;
                    x2 = f9d;
                    y1 = f6;
                    y2 = f6d;
                    break;

                case 1:
                    x1 = f6;
                    x2 = f6d;
                    y1 = f3;
                    y2 = f9d;
                    break;

                case 2:
                    x1 = f9;
                    x2 = f3d;
                    y1 = f6;
                    y2 = f6d;
                    break;

                case 3:
                    x1 = f6;
                    x2 = f6d;
                    y1 = f9;
                    y2 = f3d;
                    break;

                default:
                    x1 = y1 = 0f;
                    x2 = y2 = 1f;
                    break;
            }

            if (isBlockTopPart(meta)) {
                top = f4d;
                switch (meta & 3) {
                    case 0:
                        x2 = f1d;
                        break;

                    case 1:
                        y2 = f1d;
                        break;

                    case 2:
                        x1 = f1;
                        break;

                    case 3:
                        y1 = f1;
                        break;
                }
            }
        }
    }
}