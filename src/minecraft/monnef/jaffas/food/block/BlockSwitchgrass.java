/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.base.CustomIconHelper;
import monnef.core.utils.BitHelper;
import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.ContentHolder;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static monnef.core.utils.BlockHelper.setBlock;
import static monnef.core.utils.BlockHelper.setBlockMetadata;
import static monnef.core.utils.BlockHelper.setBlockWithoutNotify;

public class BlockSwitchgrass extends BlockJaffas implements IPlantable, IFactoryHarvestable, IFactoryFertilizable, IFactoryPlantable {
    private static final int BIT_TOP = 3;
    private static final int MAX_AGE = 7;
    private static final float border = 3f * 1f / 16f;
    private static final float borderComplement = 1f - border;
    public static int maximalHeight = 4;
    private static Icon bodyIcon;
    public static final int VALUE_TOP = BitHelper.setBit(0, BIT_TOP);

    public String[] subBlockNames;

    public BlockSwitchgrass(int id, int icon) {
        super(id, icon, Material.plants);
        this.setTickRandomly(true);
        subBlockNames = new String[16];
        for (int i = 0; i < subBlockNames.length; i++) {
            subBlockNames[i] = "Switchgrass";
        }
        setBlockBounds(border, 0, border, borderComplement, 1, borderComplement);
        setUnlocalizedName("blockJSwitchgrass");
        setBurnProperties(blockID, 60, 100);
    }

    // inspired by cactus
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isRemote) return;
        tryGrow(world, x, y, z, rand);
        if (rand.nextInt(5) == 1) checkNeighboursForWinter(world, x, y, z);
    }

    private void checkNeighboursForWinter(World world, int x, int y, int z) {
        for (int xx = x - 1; xx <= x + 1; xx++) {
            for (int yy = y - 1; yy <= y + 1; yy++) {
                for (int zz = z - 1; zz <= z + 1; zz++) {
                    int bId = world.getBlockId(xx, yy, zz);
                    if (BlockHelper.isWinterBlock(bId)) {
                        destroyWithDrop(world, x, y, z);
                        if (JaffasFood.rand.nextInt(3) == 0) {
                            world.setBlock(x, y, z, Block.snow.blockID);
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
            while (world.getBlockId(x, y - height, z) == this.blockID) height++;
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
                    setBlockWithoutNotify(world, x, y + 1, z, this.blockID, VALUE_TOP);
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
    public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
        if (!this.canBlockStay(world, x, y, z)) {
            destroyWithDrop(world, x, y, z);
        }
    }

    private void destroyWithDrop(World world, int x, int y, int z) {
        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        setBlock(world, x, y, z, 0);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int myMeta = world.getBlockMetadata(x, y, z);
        int myId = world.getBlockId(x, y, z);
        int topBlock = world.getBlockId(x, y + 1, z);
        int bottomBlock = world.getBlockId(x, y - 1, z);

        boolean top = false;
        boolean bottom = false;

        if (isTop(myMeta)) {
            if (topBlock == 0) {
                top = true;
            }
        } else {
            if (topBlock == blockID) {
                top = true;
            }
        }

        if (bottomBlock == blockID || floorCanSustainPlant(world, x, y, z)) {
            bottom = true;
        }

        // fix for Forge grass planting (it calls this method on an air block)
        if (bottom && myId == 0) return true;

        return top && bottom;
    }

    public boolean floorCanSustainPlant(World world, int x, int y, int z) {
        int blockId = world.getBlockId(x, y - 1, z);
        return blocksList[blockId] != null && blocksList[blockId].canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {
        return blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return -1;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, VALUE_TOP));
    }

    @Override
    public Icon getIcon(int par1, int par2) {
        return isTop(par2) ? blockIcon : bodyIcon;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (JaffasFood.debug) {
            int meta = par1World.getBlockMetadata(par2, par3, par4);
            if (par5EntityPlayer.isSneaking()) {
                setBlockMetadata(par1World, par2, par3, par4, MAX_AGE | (isTop(meta) ? 8 : 0));
            }
            par5EntityPlayer.addChatMessage("meta: " + meta);
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
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
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
        while (w.getBlockId(x, lowerY, z) == blockID) {
            lowerY--;
            if (y - lowerY > maximalHeight) break;
        }
        lowerY++; // fix to point on a lowest grass block, not on a soil
        return lowerY;
    }

    public int getTopY(World w, int x, int y, int z) {
        int higherY = y;
        while (!isTop(w.getBlockMetadata(x, higherY, z)) && w.getBlockId(x, higherY, z) == blockID) {
            higherY++;
            if (higherY - y > maximalHeight) break;
        }
        return higherY;
    }

    public int calculateHeight(World w, int x, int y, int z) {
        if (w.getBlockId(x, y, z) != blockID) return 0;
        int lowerY = getBaseY(w, x, y, z);
        int higherY = getTopY(w, x, y, z);
        return higherY - lowerY + 1;
    }

    // MFR
    @Override
    public int getFertilizableBlockId() {
        return blockID;
    }

    @Override
    public boolean canFertilizeBlock(World world, int x, int y, int z, FertilizerType fertilizerType) {
        return canGrow(world, x, y, z);
    }

    @Override
    public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType) {
        return tryBonemeal(world, x, y, z);
    }

    @Override
    public int getPlantId() {
        return blockID;
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
            setBlockWithoutNotify(world, x, i, z, 0, 0);
        for (int i = lowerY; i <= topY; i++)
            world.markBlockForUpdate(x, i, z);
    }

    @Override
    public int getSeedId() {
        return blockID;
    }

    @Override
    public int getPlantedBlockId(World world, int x, int y, int z, ItemStack stack) {
        return blockID;
    }

    @Override
    public int getPlantedBlockMetadata(World world, int x, int y, int z, ItemStack stack) {
        return VALUE_TOP;
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        return world.getBlockId(x, y, z) == 0 && floorCanSustainPlant(world, x, y, z);
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
    }
}
