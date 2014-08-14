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
import monnef.core.utils.WorldHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
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

public class BlockJaffaCrops extends BlockTrees implements IPlantable, IFactoryHarvestable, IFactoryFertilizable {
    private int phasesMax; // 7
    private Item product = Items.wheat;
    private Item seeds = Items.wheat_seeds;

    // 1 - "Crossed Squares" (Flowers, reeds, etc)
    // 6 - Crops
    private int renderer;
    private int baseTexture;

    public BlockJaffaCrops(int textureIndex, int phasesMax, int renderer) {
        super(textureIndex, Material.plants);
        baseTexture = textureIndex;
        this.setTickRandomly(true);
        float half = 0.5F;
        this.setBlockBounds(0.5F - half, 0.0F, 0.5F - half, 0.5F + half, 0.25F, 0.5F + half);
        this.setCreativeTab(null);
        this.phasesMax = phasesMax;
        this.renderer = renderer;
        BlockMonnefCore$.MODULE$.queueSetBurnProperties(this, 60, 100);
        setIconsCount(phasesMax + 1);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);

        if (world.getBlockLightValue(x, y + 1, z) >= 9) {
            int meta = world.getBlockMetadata(x, y, z);

            if (canGrow(meta)) {
                float growthRate = this.getGrowthRate(world, x, y, z);

                if (random.nextInt((int) (25.0F / growthRate) + 1) == 0) {
                    boolean rain = world.isRaining() && world.canBlockSeeTheSky(x, y, z);
                    // slow grow a bit
                    if (random.nextInt(4) == 0 || rain) {
                        growABit(world, x, y, z);
                    }
                }
            }
        }

        this.checkAndDropBlock(world, x, y, z);
    }

    public boolean canGrow(int meta) {
        return !isMature(meta);
    }

    public boolean isMature(int meta) {
        return meta >= phasesMax;
    }

    public boolean growABit(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (canGrow(meta)) {
            setBlockMetadata(world, x, y, z, meta + 1);
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
    private float getGrowthRate(World world, int x, int y, int z) {
        float res = 1.0F;
        Block neighZN = world.getBlock(x, y, z - 1);
        Block neighZP = world.getBlock(x, y, z + 1);
        Block neighXN = world.getBlock(x - 1, y, z);
        Block neighXP = world.getBlock(x + 1, y, z);
        Block diagNN = world.getBlock(x - 1, y, z - 1);
        Block diagPN = world.getBlock(x + 1, y, z - 1);
        Block diagPP = world.getBlock(x + 1, y, z + 1);
        Block diagNP = world.getBlock(x - 1, y, z + 1);
        boolean nieghX = neighXN == this || neighXP == this;
        boolean neighY = neighZN == this || neighZP == this;
        boolean diagCropDetected = diagNN == this || diagPN == this || diagPP == this || diagNP == this;

        for (int xx = x - 1; xx <= x + 1; ++xx) {
            for (int zz = z - 1; zz <= z + 1; ++zz) {
                Block blockUnderCurrent = world.getBlock(xx, y - 1, zz);
                float neighbourBonus = 0.0F;

                if (blockUnderCurrent == Blocks.farmland) {
                    neighbourBonus = 1.0F;

                    if (world.getBlockMetadata(xx, y - 1, zz) > 0) {
                        neighbourBonus = 3.0F;
                    }
                }

                if (xx != x || zz != z) {
                    neighbourBonus /= 4.0F;
                }

                res += neighbourBonus;
            }
        }

        if (diagCropDetected || nieghX && neighY) {
            res /= 2.0F;
        }

        return res;
    }

    @Override
    public int getRenderType() {
        return this.renderer;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if (isMature(metadata)) {
            ret.add(constructProduct());
        }

        if (isMature(metadata)) {
            for (int n = 0; n < 5 + fortune; n++) {
                if (world.rand.nextInt((15 - 7) + phasesMax) <= metadata) {
                    ret.add(constructSeeds());
                }
            }
        }

        return ret;
    }

    public ItemStack constructProduct() {
        return new ItemStack(product);
    }

    @Override
    public Item getItemDropped(int meta, Random random, int par3) {
        return isMature(meta) ? product : null;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return constructSeeds();
    }

    public ItemStack constructSeeds() {
        return new ItemStack(seeds);
    }

    public int getPhasesMax() {
        return phasesMax;
    }

    // because of Forestry
    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileJaffaCrops();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
        this.checkAndDropBlock(world, x, y, z);
    }

    protected void checkAndDropBlock(World world, int x, int y, int z) {
        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, getBlockById(0), 0, 2);
        }
    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return getCustomIcon(meta <= phasesMax ? meta : phasesMax);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        int meta = world.getBlockMetadata(x, y, z);
        if (isMature(meta)) {
            WorldHelper.dropItem(world, x, y + 1, z, constructProduct());
            setBlockMetadata(world, x, y, z, phasesMax - 1);
            return true;
        } else return super.onBlockActivated(world, x, y, z, player, side, p_149727_7_, p_149727_8_, p_149727_9_);
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

    /* IPlantable */
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 0;
    }


    public void setSeeds(Item seeds) {
        this.seeds = seeds;
    }

    public void setProduct(Item product) {
        this.product = product;
    }
}
