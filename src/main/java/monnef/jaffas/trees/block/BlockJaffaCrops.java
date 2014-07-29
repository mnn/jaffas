/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.block.BlockMonnefCore$;
import monnef.core.common.CustomIconHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.common.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static monnef.core.utils.BlockHelper.setBlockMetadata;

public class BlockJaffaCrops extends BlockFlower implements IFactoryHarvestable, IFactoryFertilizable {
    //TODO rewrite to inherit from BlockJaffas

    private int phasesMax; // 7
    private Item product = Items.wheat;
    private Item seeds = Items.wheat_seeds;

    // 1 - "Crossed Squares" (Flowers, reeds, etc)
    // 6 - Crops
    private int renderer;
    private int baseTexture;

    public BlockJaffaCrops(int textureIndex, int phasesMax, Item product, Item seeds, int renderer) {
        super(0); // TODO: rewrite to inherit from our blocks
        baseTexture = textureIndex;
        this.setTickRandomly(true);
        float var3 = 0.5F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.25F, 0.5F + var3);
        this.setCreativeTab((CreativeTabs) null);
        this.phasesMax = phasesMax;
        this.product = product;
        this.seeds = seeds;
        this.renderer = renderer;
        BlockMonnefCore$.MODULE$.setBurnProperties(this, 60, 100);
    }

    @Override
    protected boolean canPlaceBlockOn(Block p_149854_1_) {
        return p_149854_1_ == Blocks.farmland;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        if (!super.canBlockStay(world, x, y, z)) return false;
        return world.getBlock(x, y - 1, z) == Blocks.farmland;
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        super.updateTick(par1World, par2, par3, par4, par5Random);

        if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) {
            int meta = par1World.getBlockMetadata(par2, par3, par4);

            if (canGrow(meta)) {
                float var7 = this.getGrowthRate(par1World, par2, par3, par4);

                if (par5Random.nextInt((int) (25.0F / var7) + 1) == 0) {
                    // slow grow a bit
                    if (par5Random.nextInt(4) == 0) {
                        growABit(par1World, par2, par3, par4);
                    }
                }
            }
        }
    }

    public boolean canGrow(int meta) {
        return meta < phasesMax;
    }

    public boolean growABit(World par1World, int par2, int par3, int par4) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        if (canGrow(meta)) {
            setBlockMetadata(par1World, par2, par3, par4, meta + 1);
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onBonemeal(BonemealEvent event) {
        if (!(event.block instanceof BlockJaffaCrops)) {
            return;
        }

        if (tryBonemeal(event.world, event.x, event.y, event.z)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    public boolean tryBonemeal(World w, int x, int y, int z) {
        if (JaffasTrees.bonemealingAllowed && canGrow(w.getBlockMetadata(x, y, z))) {
            if (!w.isRemote) {
                if (JaffasFood.rand.nextFloat() < 0.4) growABit(w, x, y, z);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the growth rate for the crop. Setup to encourage rows by halving growth rate if there is diagonals, crops on
     * different sides that aren't opposing, and by adding growth for every crop next to this one (and for crop below
     * this one). Args: x, y, z
     */
    private float getGrowthRate(World par1World, int par2, int par3, int par4) {
        float res = 1.0F;
        Block var6 = par1World.getBlock(par2, par3, par4 - 1);
        Block var7 = par1World.getBlock(par2, par3, par4 + 1);
        Block var8 = par1World.getBlock(par2 - 1, par3, par4);
        Block var9 = par1World.getBlock(par2 + 1, par3, par4);
        Block var10 = par1World.getBlock(par2 - 1, par3, par4 - 1);
        Block var11 = par1World.getBlock(par2 + 1, par3, par4 - 1);
        Block var12 = par1World.getBlock(par2 + 1, par3, par4 + 1);
        Block var13 = par1World.getBlock(par2 - 1, par3, par4 + 1);
        boolean var14 = var8 == this || var9 == this;
        boolean var15 = var6 == this || var7 == this;
        boolean var16 = var10 == this || var11 == this || var12 == this || var13 == this;

        for (int var17 = par2 - 1; var17 <= par2 + 1; ++var17) {
            for (int var18 = par4 - 1; var18 <= par4 + 1; ++var18) {
                Block var19 = par1World.getBlock(var17, par3 - 1, var18);
                float neighbourBonus = 0.0F;

                if (var19 == Blocks.farmland) {
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
    @Override
    public IIcon getIcon(int par1, int par2) {
        if (par2 < 0) {
            par2 = phasesMax;
        }

        return icons[par2];
    }

    private IIcon[] icons;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        icons = new IIcon[phasesMax + 1];
        for (int i = 0; i <= phasesMax; i++) {
            icons[i] = register.registerIcon(CustomIconHelper.generateId(Reference.ModName, 2, baseTexture + i));
        }
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        return this.renderer;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if (metadata == phasesMax) {
            ret.add(new ItemStack(product));
        }

        for (int n = 0; n < 5 + fortune; n++) {
            if (world.rand.nextInt(10 + phasesMax) <= metadata) {
                ret.add(new ItemStack(seeds));
            }
        }

        return ret;
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return par1 == phasesMax ? product : null;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(seeds);
    }

    public int getPhasesMax() {
        return phasesMax;
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setTileEntity(par2, par3, par4, this.createNewTileEntity(par1World));
    }

    public TileEntity createNewTileEntity(World world) {
        return new TileJaffaCrops();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    /* Mine Factory Reloaded */
    @Override
    public Block getPlant() {
        return this;
    }

    @Override
    public HarvestType getHarvestType() {
        return HarvestType.Normal;
    }

    @Override
    public boolean breakBlock() {
        return true;
    }

    @Override
    public boolean canBeHarvested(World world, Map<String, Boolean> harvesterSettings, int x, int y, int z) {
        return !canGrow(world.getBlockMetadata(x, y, z));
    }

    @Override
    public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> harvesterSettings, int x, int y, int z) {
        return getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
    }

    @Override
    public void preHarvest(World world, int x, int y, int z) {
    }

    @Override
    public void postHarvest(World world, int x, int y, int z) {
    }

    @Override
    public boolean canFertilize(World world, int x, int y, int z, FertilizerType fertilizerType) {
        return canGrow(world.getBlockMetadata(x, y, z));
    }

    @Override
    public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType) {
        return tryBonemeal(world, x, y, z);
    }
}
