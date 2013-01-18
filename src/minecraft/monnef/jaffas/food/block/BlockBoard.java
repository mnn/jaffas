package monnef.jaffas.food.block;

import monnef.core.BitHelper;
import monnef.jaffas.food.client.GuiHandler;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBoard extends BlockContainer {
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

    private static int knifeBit = 2;

    public BlockBoard(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
        setRequiresSelfNotify();
        setCreativeTab(mod_jaffas.CreativeTab);
        setHardness(0.2f);
        setBlockName("blockBoard");
    }

    public String getTextureFile() {
        return "/jaffas_01.png";
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int idk, float what, float these, float are) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }

        player.openGui(mod_jaffas.instance, GuiHandler.GuiTypes.BOARD.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z) {
        Random rand = new Random();

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != null && item.stackSize > 0) {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                        new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                if (item.hasTagCompound()) {
                    entityItem.func_92014_d().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityBoard();
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLiving entity) {
        int var = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        var = (var + 0) % 4; // rotation fix
        w.setBlockMetadata(x, y, z, var);
    }

    public boolean hasKnife(int meta) {
        return BitHelper.isBitSet(meta, knifeBit);
    }

    public void setKnife(boolean present, World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlockMetadata(x, y, z, BitHelper.setBitToValue(meta, knifeBit, present));
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
        return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double) ((float) par2 + 0), (double) ((float) par3 + 0), (double) ((float) par4 + 0), (double) ((float) par2 + 1), (double) ((float) par3 + 0.5), (double) ((float) par4 + 1));
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
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            if (!par1World.isRemote) {
                this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
            }
        }
    }


}
