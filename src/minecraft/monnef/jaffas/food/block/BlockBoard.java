/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.utils.BitHelper;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.InventoryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.client.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.setBlockMetadata;

public class BlockBoard extends BlockContainerJaffas {
    public final static float unit = 1f / 16f;
    public final static float f1 = unit * 1f;
    public final static float f1d = 1f - unit * 1f;
    public final static float f2 = unit * 2f;
    public final static float f2d = 1f - unit * 2f;
    public final static float f4 = unit * 4f;
    public final static float f4d = 1f - unit * 4f;
    public final static float f5 = unit * 5f;
    public final static float f5d = 1f - unit * 5f;
    public final static float f7 = unit * 7f;
    public final static float f7d = 1f - unit * 7f;
    public final static float f9d = 1f - unit * 9f;
    public final static float f10d = 1f - unit * 10f;

    protected static final int knifeBit = 2;

    public BlockBoard(int par1, int index, Material par3Material) {
        super(par1, index, par3Material);
        //setRequiresSelfNotify();
        setCreativeTab(JaffasFood.CreativeTab);
        setHardness(0.2f);
        setUnlocalizedName("blockBoard");
        setBurnProperties(blockID, 5, 5);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int idk, float what, float these, float are) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }

        player.openGui(JaffasFood.instance, GuiHandler.GuiTypes.BOARD.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        InventoryUtils.dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityBoard();
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLiving entity, ItemStack stack) {
        int var = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        var = (var + 0) % 4; // rotation fix
        setBlockMetadata(w, x, y, z, var);
    }

    public boolean hasKnife(int meta) {
        return BitHelper.isBitSet(meta, knifeBit);
    }

    public void setKnife(boolean present, World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        setBlockMetadata(world, x, y, z, BitHelper.setBitToValue(meta, knifeBit, present));
    }

    @Override
    public int getRenderType() {
        return JaffasFood.renderID;
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

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        int side = meta & 3;
        //return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double) ((float) par2 + 0), (double) ((float) par3 + 0), (double) ((float) par4 + 0), (double) ((float) par2 + 1), (double) ((float) par3 + 0.5), (double) ((float) par4 + 1));
        return AxisAlignedBB.getAABBPool().getAABB((double) ((float) par2 + 0), (double) ((float) par3 + 0), (double) ((float) par4 + 0), (double) ((float) par2 + 1), (double) ((float) par3 + 0.5), (double) ((float) par4 + 1));
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int meta = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        int side = meta & 3;

        this.setBlockBounds(0, 0, 0, 1, 0.5f, 1);
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        boolean destroy = false;

        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4)) {
            destroy = true;
        }

        if (destroy) {
            BlockHelper.setBlock(par1World, par2, par3, par4, 0);
            if (!par1World.isRemote) {
                this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
            }
        }
    }


}
