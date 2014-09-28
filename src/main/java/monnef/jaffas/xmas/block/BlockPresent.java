/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

import monnef.core.utils.PlayerHelper;
import monnef.jaffas.xmas.JaffasXmas;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.setAir;

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

    public BlockPresent(int textureID, Material material, int subBlocksCount) {
        super(textureID, material, subBlocksCount);
        setBlockName("present");
        setBurnProperties(30, 100);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);

        if (meta < 6) {
            return AxisAlignedBB.getBoundingBox((double) ((float) par2 + f2), (double) par3, (double) ((float) par4 + f2), (double) ((float) par2 + f2d), (double) ((float) par3 + f7d), (double) ((float) par4 + f2d));
        } else {
            return AxisAlignedBB.getBoundingBox((double) ((float) par2 + f4), (double) par3, (double) ((float) par4 + f4), (double) ((float) par2 + f4d), (double) ((float) par3 + f9d), (double) ((float) par4 + f4d));
        }
    }

    @Override
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
        return new TilePresent();
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
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setTileEntity(par2, par3, par4, this.createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
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

        TilePresent te = (TilePresent) par1World.getTileEntity(par2, par3, par4);

        if (player.isSneaking()) {
            ItemStack i = te.getContent();
            if (i != null) {
                String q = i.stackSize > 1 ? " x" + i.stackSize : "";
                PlayerHelper.addMessage(player, "Inside present is " + i.getDisplayName() + q + ".");
            } else {
                PlayerHelper.addMessage(player, "Present is empty.");
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        boolean destroy = false;
        int meta = world.getBlockMetadata(x, y, z);

        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            destroy = true;
        }

        if (destroy) {
            if (!world.isRemote) {
                this.dropBlockAsItem(world, x, y, z, meta, 0);
                this.dropItems(world, x, y, z);
            }
            setAir(world, x, y, z);
        }
    }

    private void dropItems(World world, int x, int y, int z) {
        TilePresent te = (TilePresent) world.getTileEntity(x, y, z);
        ItemStack item = te.getContent();
        if (item != null) {
            te.setContent(null);
            EntityItem ei = new EntityItem(world, te.xCoord + 0.5, te.yCoord + 1, te.zCoord + 0.5, item);
            world.spawnEntityInWorld(ei);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        dropItems(world, x, y, z);
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return Item.getItemFromBlock(this).getIconFromDamage(par2);
    }
}
