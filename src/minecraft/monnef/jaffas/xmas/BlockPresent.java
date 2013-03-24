package monnef.jaffas.xmas;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.setBlock;

public class BlockPresent extends BlockXmasMulti {
    public final static float unit = 1f / 16f;
    public final static float f2 = unit * 2f;
    public final static float f2d = 1f - unit * 2f;
    public final static float f4 = unit * 4f;
    public final static float f4d = 1f - unit * 4f;
    public final static float f5 = unit * 5f;
    public final static float f5d = 1f - unit * 5f;
    public final static float f7 = unit * 7f;
    public final static float f7d = 1f - unit * 7f;
    public final static float f9d = 1f - unit * 9f;

    public BlockPresent(int id, int textureID, Material material, int subBlocksCount) {
        super(id, textureID, material, subBlocksCount);
        setUnlocalizedName("present");
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);

        if (meta < 6) {
            return AxisAlignedBB.getAABBPool().getAABB((double) ((float) par2 + f2), (double) par3, (double) ((float) par4 + f2), (double) ((float) par2 + f2d), (double) ((float) par3 + f7d), (double) ((float) par4 + f2d));
        } else {
            return AxisAlignedBB.getAABBPool().getAABB((double) ((float) par2 + f4), (double) par3, (double) ((float) par4 + f4), (double) ((float) par2 + f4d), (double) ((float) par3 + f9d), (double) ((float) par4 + f4d));
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int meta = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if (meta < 6) {
            this.setBlockBounds(f2, 0f, f2, f2d, f7d, f2d);
        } else {
            this.setBlockBounds(f4, 0f, f4, f4d, f9d, f4d);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityPresent();
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
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (par1World.isRemote) {
            return true;
        }

        TileEntityPresent te = (TileEntityPresent) par1World.getBlockTileEntity(par2, par3, par4);

        if (player.isSneaking()) {
            ItemStack i = te.getContent();
            if (i != null) {
                String q = i.stackSize > 1 ? " x" + i.stackSize : "";
                player.addChatMessage("Inside present is " + i.getDisplayName() + q + ".");
            } else {
                player.addChatMessage("Present is empty.");
            }
        } else {

            if (te.getContent() == null) {
                ItemStack equippedItem = player.getCurrentEquippedItem();
                if (equippedItem != null) {
                    te.setContent(equippedItem.copy());
                    player.destroyCurrentEquippedItem();
                }
            } else {
                dropItems(par1World, par2, par3, par4);
            }
        }

        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        boolean destroy = false;
        int meta = par1World.getBlockMetadata(par2, par3, par4);

        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4)) {
            destroy = true;
        }

        if (destroy) {
            if (!par1World.isRemote) {
                this.dropBlockAsItem(par1World, par2, par3, par4, meta, 0);
                this.dropItems(par1World, par2, par3, par4);
            }
            setBlock(par1World, par2, par3, par4, 0);
        }
    }

    private void dropItems(World world, int x, int y, int z) {
        TileEntityPresent te = (TileEntityPresent) world.getBlockTileEntity(x, y, z);
        ItemStack item = te.getContent();
        if (item != null) {
            te.setContent(null);
            EntityItem ei = new EntityItem(world, te.xCoord + 0.5, te.yCoord + 1, te.zCoord + 0.5, item);
            world.spawnEntityInWorld(ei);
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        dropItems(par1World, par2, par3, par4);
    }

}
