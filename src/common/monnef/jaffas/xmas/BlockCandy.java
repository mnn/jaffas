package monnef.jaffas.xmas;

import net.minecraft.src.*;

import java.util.Random;

public class BlockCandy extends BlockXmas {

    public BlockCandy(int id, int texture, Material material) {
        super(id, texture, material);
        setRequiresSelfNotify();
        setHardness(0.5f);
        setCreativeTab(null);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityCandy();
    }

    @Override
    public int getRenderType() {
        return mod_jaffas_xmas.renderID;
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

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);

        if (isBlockTopPart(meta)) {
            if (par1World.getBlockId(par2, par3 - 1, par4) != this.blockID) {
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
        } else {
            boolean destroy = false;
            if (par1World.getBlockId(par2, par3 + 1, par4) != this.blockID) {
                destroy = true;
            }

            if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4)) {
                destroy = true;
            }

            if (destroy) {
                par1World.setBlockWithNotify(par2, par3, par4, 0);
                if (!par1World.isRemote) {
                    this.dropBlockAsItem(par1World, par2, par3, par4, meta, 0);
                }
            }

        }
    }

    public static boolean isBlockTopPart(int meta) {
        return (meta & 8) != 0;
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return isBlockTopPart(par1) ? 0 : mod_jaffas_xmas.ItemGiantCandy.shiftedIndex;
    }

    @Override
    public int idPicked(World par1World, int par2, int par3, int par4) {
        return mod_jaffas_xmas.ItemGiantCandy.shiftedIndex;
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

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int meta = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        BoundsHelper b = new BoundsHelper(meta);

        //TODO: implement getCollisionBoundingBoxFromPool for server
        this.setBlockBounds(b.x1, 0f, b.y1, b.x2, b.top, b.y2);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        BoundsHelper b = new BoundsHelper(meta);

        return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double) ((float) par2 + b.x1), (double) par3, (double) ((float) par4 + b.y1), (double) ((float) par2 + b.x2), (double) ((float) par3 + b.top), (double) ((float) par4 + b.y2));
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

    /*
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par1World.isRemote) {
            return false;

        }

        int meta = par1World.getBlockMetadata(par2, par3, par4);
        par5EntityPlayer.sendChatToPlayer("meta: " + meta);

        return true;
    }
    */
}