package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BitHelper;
import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class BlockHighPlant extends BlockTechnic implements IPlantable {
    private static final int SLAVE_BIT = 3;
    private static final int INTEGRITY_CHECK_RADIUS = 5;
    private static final int BLOCK_ACTIVATION_RADIUS = 3;
    private static final float border = 4f * 1f / 16f;
    private static final float borderComplement = 1f - border;
    private static final float U = 1 / 16f;
    private static final float topComplement = 1f - 4 * U;

    public BlockHighPlant(int textureID) {
        super(textureID, Material.plants);
        setHardness(0.33f);
        removeFromCreativeTab();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (isSlave(meta)) {
            setBlockBounds(border, 0, border, borderComplement, topComplement, borderComplement);
        } else {
            setBlockBounds(border, 0, border, borderComplement, 1, borderComplement);
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        planIntegrityCheckBellow(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    private void planIntegrityCheckBellow(World world, int x, int y, int z) {
        for (int cy = y; cy > y - INTEGRITY_CHECK_RADIUS; cy--) {
            if (cy < 0) break;
            TileEntity tile = world.getTileEntity(x, cy, z);
            if (tile instanceof TileHighPlant) {
                ((TileHighPlant) tile).planIntegrityCheck();
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        TileHighPlant te = null;

        int ny = y;
        int tested = 0;
        while (te == null && tested < BLOCK_ACTIVATION_RADIUS) {
            te = (TileHighPlant) world.getTileEntity(x, ny, z);
            ny--;
            tested++;
        }

        boolean res = te.playerActivatedBox(player);
        if (MonnefCorePlugin.debugEnv) {
            te.printDebugInfo(player);
        }

        return res;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return isMaster(meta);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileHighPlant();
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
        return validSoil(world, x, y - 1, z) || world.getBlock(x, y - 1, z) == this;
    }

    public boolean validSoil(World world, int x, int y, int z) {
        Block bellow = world.getBlock(x, y, z);
        if (bellow == null) return false;
        return bellow.canSustainPlant(world, x, y, z, ForgeDirection.UP, this);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return canBlockStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbour) {
        boolean drop = false;
        if (!canBlockStay(world, x, y, z)) {
            drop = true;
        }

        if (drop && !world.isRemote) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0); // metadata, fortune
            BlockHelper.setAir(world, x, y, z);
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> res = new ArrayList<ItemStack>();

        if (isMaster(metadata)) {
            res.add(new ItemStack(JaffasTechnic.highPlantPost));
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
