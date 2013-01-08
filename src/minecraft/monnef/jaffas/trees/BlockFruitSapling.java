package monnef.jaffas.trees;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFlower;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.List;
import java.util.Random;

public class BlockFruitSapling extends BlockFlower {
    public static Random rand = new Random();
    public int serialNumber = -1;

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        // bonemeal
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
        if (itemstack != null && itemstack.itemID == Item.dyePowder.shiftedIndex && mod_jaffas_trees.bonemealingAllowed) {
            if (itemstack.getItemDamage() == 15) {
                if (!par1World.isRemote) {
                    growTree(par1World, par2, par3, par4, rand);
                    itemstack.stackSize--;
                }
            }
        }
        super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        return true;
    }

    protected BlockFruitSapling(int blockId, int blockIndexInTexture) {
        super(blockId, blockIndexInTexture);
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (!par1World.isRemote) {
            super.updateTick(par1World, par2, par3, par4, par5Random);

            if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 && par5Random.nextInt(7) == 0) {
                int metadata = par1World.getBlockMetadata(par2, par3, par4);

                if (mod_jaffas_trees.debug) {
                    System.out.println("meta(" + metadata + ") markForDecay("
                            + BlockFruitLeaves.areLeavesMarkedForDecay(metadata) + ") setLeavesDecay("
                            + BlockFruitLeaves.setLeavesDecay(metadata) + ") areAfterSet("
                            + BlockFruitLeaves.areLeavesMarkedForDecay(BlockFruitLeaves.setLeavesDecay(metadata)) + ")");
                }

                //if ((var6 & 8) == 0) {
                if (!BlockFruitLeaves.areLeavesMarkedForDecay(metadata)) {
                    //par1World.setBlockMetadataWithNotify(par2, par3, par4, BlockFruitLeaves.setLeavesDecay(metadata));
                    par1World.setBlockMetadata(par2, par3, par4, BlockFruitLeaves.setLeavesDecay(metadata));
                    if (mod_jaffas_trees.debug) {
                        System.out.println("after set: " + par1World.getBlockMetadata(par2, par3, par4));
                    }
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
        int metadata = par1World.getBlockMetadata(par2, par3, par4);
        metadata = BlockFruitLeaves.getLeavesType(metadata);
        Object var7 = null;
        int var8 = 0;
        int var9 = 0;

        par1World.setBlock(par2, par3, par4, 0);
        /*
    this.minTreeHeight = par2;
    this.metaWood = par3;
    this.metaLeaves = par4;
    this.vinesGrow = par5;  */

        var7 = new WorldGenFruitTrees(true, 5, 0, metadata, false, mod_jaffas_trees.leavesList.get(serialNumber).leavesID);

        if (!((WorldGenerator) var7).generate(par1World, par5Random, par2 + var8, par3, par4 + var9)) {
            par1World.setBlockAndMetadata(par2, par3, par4, this.blockID, metadata);
        }
    }

    /**
     * Determines if the same sapling is present at the given location.
     */
    public boolean isSameSapling(World par1World, int par2, int par3, int par4, int par5) {
        return par1World.getBlockId(par2, par3, par4) == this.blockID && (BlockFruitLeaves.getLeavesType(par1World.getBlockMetadata(par2, par3, par4))) == par5;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1) {
        return BlockFruitLeaves.getLeavesType(par1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
/*        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));*/
    }
}
