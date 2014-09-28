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
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
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

    public BlockBoard(int index, Material material) {
        super(index, material);
        //setRequiresSelfNotify();
        setCreativeTab(JaffasFood.instance.creativeTab);
        setHardness(0.2f);
        setBlockName("blockBoard");
        setBurnProperties(5, 5);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int idk, float what, float these, float are) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }

        player.openGui(JaffasFood.instance, GuiHandler.GuiTypes.BOARD.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        InventoryUtils.dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileBoard();
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
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
        return ContentHolder.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        int side = meta & 3;
        return AxisAlignedBB.getBoundingBox((double) ((float) x + 0), (double) ((float) y + 0), (double) ((float) z + 0), (double) ((float) x + 1), (double) ((float) y + 0.5), (double) ((float) z + 1));
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        int side = meta & 3;

        this.setBlockBounds(0, 0, 0, 1, 0.5f, 1);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        boolean destroy = false;

        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            destroy = true;
        }

        if (destroy) {
            BlockHelper.setAir(world, x, y, z);
            if (!world.isRemote) {
                this.dropBlockAsItem(world, x, y, z, 0, 0);
            }
        }
    }
}
