/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.common.CustomIconHelper;
import monnef.core.utils.BitHelper;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.common.MfrHelper;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import powercrystals.minefactoryreloaded.api.ReplacementBlock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static monnef.core.utils.BlockHelper.setBlockMetadata;
import static monnef.core.utils.BlockHelper.setBlockWithoutNotify;

public class BlockSwitchgrass extends BlockJaffas implements IPlantable, IFactoryHarvestable, IFactoryFertilizable, IFactoryPlantable {
    private static final int BIT_TOP = 3;
    private static final int MAX_AGE = 7;
    private static final float border = 3f * 1f / 16f;
    private static final float borderComplement = 1f - border;
    public static int maximalHeight = 4;
    private static IIcon bodyIcon;
    public static final int VALUE_TOP = BitHelper.setBit(0, BIT_TOP);

    public String[] subBlockNames;

    public BlockSwitchgrass(int icon) {
        super(icon, Material.plants);
        this.setTickRandomly(true);
        subBlockNames = new String[16];
        for (int i = 0; i < subBlockNames.length; i++) {
            subBlockNames[i] = "switchgrass";
        }
        setBlockBounds(border, 0, border, borderComplement, 1, borderComplement);
        setBlockName("blockJSwitchgrass");
        setBurnProperties(60, 100);
    }

    // inspired by cactus
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isRemote) return;
        tryGrow(world, x, y, z, rand);
        if (rand.nextInt(5) == 1) checkNeighboursForWinter(world, x, y, z);
    }

    private void checkNeighboursForWinter(World world, int x, int y, int z) {
        for (int xx = x - 1; xx <= x + 1; xx++) {
            for (int yy = y - 1; yy <= y + 1; yy++) {
                for (int zz = z - 1; zz <= z + 1; zz++) {
                    Block b = world.getBlock(xx, yy, zz);
                    if (BlockHelper.isWinterBlock(b)) {
                        destroyWithDrop(world, x, y, z);
                        if (JaffasFood.rand.nextInt(3) == 0) {
                            BlockHelper.setBlock(world, x, y, z, Blocks.snow);
                        }
                    }
                }
            }
        }
    }

    private void tryGrow(World world, int x, int y, int z, Random rand) {
        if (world.isRemote) return;
        int myMeta = world.getBlockMetadata(x, y, z);
        if (isTop(myMeta) && world.isAirBlock(x, y + 1, z)) {
            int height = 1;
            while (world.getBlock(x, y - height, z) == this) height++;
            int light = world.getBlockLightValue(x, y + 1, z);
            boolean onRain = world.canLightningStrikeAt(x, y + 1, z);

            if (height < maximalHeight && (light > 5 || onRain) &&
                    (light > 9 || onRain || rand.nextBoolean())) {
                int age = getAge(myMeta);
                if (age < MAX_AGE) {
                    int newMeta = setAge(myMeta, age + 1 + (onRain ? 2 : 0));
                    setBlockMetadata(world, x, y, z, newMeta);
                    //Log.printDebug(myMeta + " -> " + newMeta);
                } else {
                    setBlockWithoutNotify(world, x, y + 1, z, this, VALUE_TOP);
                    setBlockMetadata(world, x, y, z, 0); // no longer the top one
                    world.markBlockForUpdate(x, y + 1, z);
                }
            }
        }
    }

    private int setAge(int meta, int age) {
        if (age > MAX_AGE) {
            age = MAX_AGE;
        }

        //return (isTop(meta) ? 8 : 0) | age;
        return BitHelper.setBitToValue(age, BIT_TOP, isTop(meta));
    }

    public boolean isTop(int meta) {
        return BitHelper.isBitSet(meta, BIT_TOP);
    }

    public int getAge(int meta) {
        return BitHelper.unsetBit(meta, BIT_TOP);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ContentHolder.renderSwitchgrassID;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int x, int y, int z) {
        return super.canPlaceBlockAt(par1World, x, y, z) && this.floorCanSustainPlant(par1World, x, y, z);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!this.canBlockStay(world, x, y, z)) {
            destroyWithDrop(world, x, y, z);
        }
    }

    private void destroyWithDrop(World world, int x, int y, int z) {
        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        BlockHelper.setAir(world, x, y, z);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int myMeta = world.getBlockMetadata(x, y, z);
        Block me = world.getBlock(x, y, z);
        Block topBlock = world.getBlock(x, y + 1, z);
        Block bottomBlock = world.getBlock(x, y - 1, z);

        boolean top = false;
        boolean bottom = false;

        if (isTop(myMeta)) {
            if (world.isAirBlock(x, y + 1, z)) {
                top = true;
            }
        } else {
            if (topBlock == this) {
                top = true;
            }
        }

        if (bottomBlock == this || floorCanSustainPlant(world, x, y, z)) {
            bottom = true;
        }

        // fix for Forge grass planting (it calls this method on an air block)
        if (bottom && me == null) return true;

        return top && bottom;
    }

    public boolean floorCanSustainPlant(World world, int x, int y, int z) {
        Block floor = world.getBlock(x, y - 1, z);
        return floor.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
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
        return -1;
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, VALUE_TOP));
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return isTop(par2) ? blockIcon : bodyIcon;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (JaffasFood.debug) {
            int meta = world.getBlockMetadata(x, y, z);
            if (player.isSneaking()) {
                setBlockMetadata(world, x, y, z, MAX_AGE | (isTop(meta) ? 8 : 0));
            }
            PlayerHelper.addMessage(player, "meta: " + meta);
        }

        return false;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        double rnd = par1Random.nextDouble();
        if (rnd < .2) {
            return 2;
        } else if (rnd < .7) {
            return 1;
        }
        return 0;
    }

    @Override
    public int damageDropped(int par1) {
        return VALUE_TOP;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerBlockIcons(iconRegister);
        bodyIcon = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, 1));
    }

    public boolean tryBonemeal(World w, int x, int y, int z) {
        if (JaffasTrees.bonemealingAllowed) {
            if (!w.isRemote) {
                int height = calculateHeight(w, x, y, z);
                if (isFullyGrown(height)) return false;
                int higherY = getTopY(w, x, y, z);
                if (JaffasFood.rand.nextFloat() < 1.0) {
                    tryGrow(w, x, higherY, z, JaffasFood.rand);
                }
                w.playAuxSFX(2005, x, higherY, z, 0);
                return true;
            }
        }
        return false;
    }

    public boolean canGrow(World w, int x, int y, int z) {
        return canGrow(calculateHeight(w, x, y, z));
    }

    public boolean canGrow(int height) {
        return height < maximalHeight;
    }

    public boolean isFullyGrown(World w, int x, int y, int z) {
        return isFullyGrown(calculateHeight(w, x, y, z));
    }

    public boolean isFullyGrown(int height) {
        return height >= maximalHeight;
    }

    public int getBaseY(World w, int x, int y, int z) {
        int lowerY = y;
        while (w.getBlock(x, lowerY, z) == this) {
            lowerY--;
            if (y - lowerY > maximalHeight) break;
        }
        lowerY++; // fix to point on a lowest grass block, not on a soil
        return lowerY;
    }

    public int getTopY(World w, int x, int y, int z) {
        int higherY = y;
        while (!isTop(w.getBlockMetadata(x, higherY, z)) && w.getBlock(x, higherY, z) == this) {
            higherY++;
            if (higherY - y > maximalHeight) break;
        }
        return higherY;
    }

    public int calculateHeight(World w, int x, int y, int z) {
        if (w.getBlock(x, y, z) != this) return 0;
        int lowerY = getBaseY(w, x, y, z);
        int higherY = getTopY(w, x, y, z);
        return higherY - lowerY + 1;
    }


    // TODO: new MFR API
    // MFR

    @Override
    public boolean canFertilize(World world, int x, int y, int z, FertilizerType fertilizerType) {
        return canGrow(world, x, y, z);
    }

    @Override
    public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType) {
        return tryBonemeal(world, x, y, z);
    }

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
        return false;
    }

    @Override
    public boolean canBeHarvested(World world, Map<String, Boolean> harvesterSettings, int x, int y, int z) {
        return isFullyGrown(world, x, y, z);
    }

    @Override
    public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> harvesterSettings, int x, int y, int z) {
        int countToDrop = 0;
        int height = calculateHeight(world, x, y, z);

        for (int i = 0; i < height; i++) {
            countToDrop += quantityDropped(rand);
        }

        return Arrays.asList(new ItemStack(this, countToDrop, VALUE_TOP));
    }

    @Override
    public void preHarvest(World world, int x, int y, int z) {
    }

    @Override
    public void postHarvest(World world, int x, int y, int z) {
        int lowerY = getBaseY(world, x, y, z);
        int topY = getTopY(world, x, y, z);
        for (int i = lowerY; i <= topY; i++)
            setBlockWithoutNotify(world, x, i, z, Blocks.air, 0);
        for (int i = lowerY; i <= topY; i++)
            world.markBlockForUpdate(x, i, z);
    }

    @Override
    public boolean canBePlanted(ItemStack stack, boolean forFermenting) {
        return true;
    }

    @Override
    public ReplacementBlock getPlantedBlock(World world, int x, int y, int z, ItemStack stack) {
        return MfrHelper.replacementBlockWithMeta(this, VALUE_TOP);
    }

    @Override
    public Item getSeed() {
        return Item.getItemFromBlock(this);
    }


    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        return world.isAirBlock(x, y, z) && floorCanSustainPlant(world, x, y, z);
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
    }
}
