package monnef.jaffas.trees;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Random;

public class BlockJaffaCrops extends BlockFlower {

    private int phasesMax; // 7
    private Item product = Item.wheat;
    private Item seeds = Item.seeds;

    // 1 - "Crossed Squares" (Flowers, reeds, etc)
    // 6 - Crops
    private int renderer;

    protected BlockJaffaCrops(int blockID, int textureIndex, int phasesMax, Item product, Item seeds, int renderer) {
        super(blockID, textureIndex);
        this.blockIndexInTexture = textureIndex;
        this.setTickRandomly(true);
        float var3 = 0.5F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.25F, 0.5F + var3);
        this.setCreativeTab((CreativeTabs) null);
        this.phasesMax = phasesMax;
        this.product = product;
        this.seeds = seeds;
        this.renderer = renderer;
    }

    public String getTextureFile() {
        return "/jaffas_02.png";
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        // bonemeal
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
        if (itemstack != null && itemstack.itemID == Item.dyePowder.shiftedIndex && mod_jaffas_trees.bonemealingAllowed) {
            if (itemstack.getItemDamage() == 15) {
                //growTree(par1World, par2, par3, par4, rand);
                this.fertilize(par1World, par2, par3, par4);
                itemstack.stackSize--;
            }
        }
        super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        return true;
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. Args: blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int par1) {
        return par1 == Block.tilledField.blockID;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        super.updateTick(par1World, par2, par3, par4, par5Random);

        if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) {
            int var6 = par1World.getBlockMetadata(par2, par3, par4);

            if (var6 < phasesMax) {
                float var7 = this.getGrowthRate(par1World, par2, par3, par4);

                if (par5Random.nextInt((int) (25.0F / var7) + 1) == 0) {
                    ++var6;
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var6);
                }
            }
        }
    }

    /**
     * Apply bonemeal to the crops.
     */
    public void fertilize(World par1World, int par2, int par3, int par4) {
        par1World.setBlockMetadataWithNotify(par2, par3, par4, phasesMax);
    }

    /**
     * Gets the growth rate for the crop. Setup to encourage rows by halving growth rate if there is diagonals, crops on
     * different sides that aren't opposing, and by adding growth for every crop next to this one (and for crop below
     * this one). Args: x, y, z
     */
    private float getGrowthRate(World par1World, int par2, int par3, int par4) {
        float res = 1.0F;
        int var6 = par1World.getBlockId(par2, par3, par4 - 1);
        int var7 = par1World.getBlockId(par2, par3, par4 + 1);
        int var8 = par1World.getBlockId(par2 - 1, par3, par4);
        int var9 = par1World.getBlockId(par2 + 1, par3, par4);
        int var10 = par1World.getBlockId(par2 - 1, par3, par4 - 1);
        int var11 = par1World.getBlockId(par2 + 1, par3, par4 - 1);
        int var12 = par1World.getBlockId(par2 + 1, par3, par4 + 1);
        int var13 = par1World.getBlockId(par2 - 1, par3, par4 + 1);
        boolean var14 = var8 == this.blockID || var9 == this.blockID;
        boolean var15 = var6 == this.blockID || var7 == this.blockID;
        boolean var16 = var10 == this.blockID || var11 == this.blockID || var12 == this.blockID || var13 == this.blockID;

        for (int var17 = par2 - 1; var17 <= par2 + 1; ++var17) {
            for (int var18 = par4 - 1; var18 <= par4 + 1; ++var18) {
                int var19 = par1World.getBlockId(var17, par3 - 1, var18);
                float neighbourBonus = 0.0F;

                if (var19 == Block.tilledField.blockID) {
                    neighbourBonus = 1.0F;

                    if (par1World.getBlockMetadata(var17, par3 - 1, var18) > 0) {
                        neighbourBonus = 3.0F;
                    }
                }

                if (var17 != par2 || var18 != par4) {
                    neighbourBonus /= 4.0F;
                }

                res += neighbourBonus;
            }
        }

        if (var16 || var14 && var15) {
            res /= 2.0F;
        }

        return res;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
        if (par2 < 0) {
            par2 = phasesMax;
        }

        return this.blockIndexInTexture + par2;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return this.renderer;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if (metadata == phasesMax) {
            ret.add(new ItemStack(product));
        }

        for (int n = 0; n < 3 + fortune; n++) {

            if (world.rand.nextInt(8 + phasesMax) <= metadata) {
                ret.add(new ItemStack(seeds));
            }
        }

        return ret;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3) {
        return par1 == phasesMax ? product.shiftedIndex : -1;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random) {
        return 1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4) {
        return seeds.shiftedIndex;
    }
}
