/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BitHelper;
import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockFermenter extends BlockTechnic {
    private static final int SLAVE_BIT = 3;
    private static final int BLOCK_ACTIVATION_RADIUS = 2;
    private static final float border = 2f * 1f / 16f;
    private static final float borderComplement = 1f - border;
    private static final float topBorder = 8f * 1f / 16f;
    private static final float topBorderComplement = 1f - topBorder;

    public BlockFermenter(int id, int textureID) {
        super(id, textureID, JaffasTechnic.breakableIronMaterial);
        setHardness(3f);
        if (!MonnefCorePlugin.debugEnv) setCreativeTab(null);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (isSlave(meta)) {
            setBlockBounds(border, 0, border, borderComplement, topBorderComplement, borderComplement);
        } else if (isMaster(meta)) {
            setBlockBounds(border, topBorder, border, borderComplement, 1, borderComplement);
        } else {
            throw new RuntimeException("invalid block state");
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public boolean isMaster(int meta) {
        return !isSlave(meta);
    }

    public boolean isSlave(int meta) {
        return BitHelper.isBitSet(meta, SLAVE_BIT);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int meta) {
        planIntegrityCheckBellow(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, meta);
    }

    private void planIntegrityCheckBellow(World world, int x, int y, int z) {
        int cy = y - 1;
        TileEntity tile = world.getBlockTileEntity(x, cy, z);
        if (tile instanceof TileFermenter) {
            ((TileFermenter) tile).planIntegrityCheck();
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        TileEntity te = null;

        int ny = y;
        int tested = 0;
        while (!(te instanceof TileFermenter) && tested < BLOCK_ACTIVATION_RADIUS) {
            te = world.getBlockTileEntity(x, ny, z);
            ny--;
            tested++;
        }

        if (!(te instanceof TileFermenter)) {
            JaffasFood.Log.printWarning("Fermenter: block activation problem, cannot find my TE");
            return false;
        }

        return ((TileFermenter) te).playerActivatedBox(player);
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return isMaster(metadata) ? new TileFermenter() : new TileFermenterInventoryRouter();
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
    public boolean canBlockStay(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return isMaster(meta) || world.getBlockId(x, y - 1, z) == blockID;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return canBlockStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
        boolean drop = false;
        if (!canBlockStay(world, x, y, z)) {
            drop = true;
        }

        if (drop && !world.isRemote) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0); // metadata, fortune
            BlockHelper.setBlock(world, x, y, z, 0);
        }
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> res = new ArrayList<ItemStack>();

        if (isMaster(metadata)) {
            res.add(new ItemStack(JaffasTechnic.itemFermenter));
        }

        return res;
    }

    public int setDirection(int meta, int direction) {
        return meta | (direction & 3);
    }

    public int setMaster(int meta) {
        return BitHelper.unsetBit(meta, SLAVE_BIT);
    }

    public int setSlave(int meta) {
        return BitHelper.setBit(meta, SLAVE_BIT);
    }
}
