package jaffas.trees;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.*;

import java.util.List;
import java.util.Random;

public class BlockFruitSapling extends BlockFlower {
    public static final String[] field_72270_a = new String[]{"apple", "spruce", "birch", "jungle"};
    public static Random rand = new Random();

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
        if (itemstack != null && itemstack.itemID == Item.dyePowder.shiftedIndex) {
            if (itemstack.getItemDamage() == 15) {
                growTree(par1World, par2, par3, par4, rand);
                itemstack.stackSize--;
            }
        }
        super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        return true;
    }

    protected BlockFruitSapling(int blockId, int blockIndexInTexture) {
        super(blockId, blockIndexInTexture);
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setCreativeTab(CreativeTabs.tabDeco);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (!par1World.isRemote) {
            super.updateTick(par1World, par2, par3, par4, par5Random);

            if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 && par5Random.nextInt(7) == 0) {
                int var6 = par1World.getBlockMetadata(par2, par3, par4);

                if ((var6 & 8) == 0) {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 | 8);
                } else {
                    this.growTree(par1World, par2, par3, par4, par5Random);
                }
            }
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
/*        par2 &= 3;
        return par2 == 1 ? 63 : (par2 == 2 ? 79 : (par2 == 3 ? 30 : super.getBlockTextureFromSideAndMetadata(par1, par2)));*/
        return 63;
    }

    /**
     * Attempts to grow a sapling into a tree
     */
    public void growTree(World par1World, int par2, int par3, int par4, Random par5Random) {
        int metadata = par1World.getBlockMetadata(par2, par3, par4) & 3;
        Object var7 = null;
        int var8 = 0;
        int var9 = 0;
        boolean var10 = false;

        par1World.setBlock(par2, par3, par4, 0);
        /*
    this.minTreeHeight = par2;
    this.metaWood = par3;
    this.metaLeaves = par4;
    this.vinesGrow = par5;  */

        var7 = new WorldGenFruitTrees(true, 5, metadata, metadata, false);

        if (!((WorldGenerator) var7).generate(par1World, par5Random, par2 + var8, par3, par4 + var9)) {
            if (var10) {
                par1World.setBlockAndMetadata(par2 + var8, par3, par4 + var9, this.blockID, metadata);
                par1World.setBlockAndMetadata(par2 + var8 + 1, par3, par4 + var9, this.blockID, metadata);
                par1World.setBlockAndMetadata(par2 + var8, par3, par4 + var9 + 1, this.blockID, metadata);
                par1World.setBlockAndMetadata(par2 + var8 + 1, par3, par4 + var9 + 1, this.blockID, metadata);
            } else {
                par1World.setBlockAndMetadata(par2, par3, par4, this.blockID, metadata);
            }
        }
    }

    /**
     * Determines if the same sapling is present at the given location.
     */
    public boolean isSameSapling(World par1World, int par2, int par3, int par4, int par5) {
        return par1World.getBlockId(par2, par3, par4) == this.blockID && (par1World.getBlockMetadata(par2, par3, par4) & 3) == par5;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    protected int damageDropped(int par1) {
        return par1 & 3;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }
}
