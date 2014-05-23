/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.block.BlockJaffas;
import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.common.Reference;
import monnef.jaffas.trees.common.WorldGenFruitTrees;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;

import java.util.List;
import java.util.Random;

import static monnef.core.utils.BlockHelper.setBlock;
import static monnef.core.utils.BlockHelper.setBlockMetadata;
import static monnef.jaffas.food.JaffasFood.Log;
import static net.minecraftforge.common.EnumPlantType.Plains;

public class BlockFruitSapling extends BlockJaffas implements IPlantable, IFactoryPlantable, IFactoryFertilizable {
    public static Random rand = new Random();
    private final int subCount;
    public int serialNumber = -1;

    public BlockFruitSapling(int blockId, int blockIndexInTexture, int subCount) {
        super(blockId, blockIndexInTexture, Material.plants);
        this.subCount = subCount;
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        setCreativeTab(JaffasTrees.instance.creativeTab);
        this.setTickRandomly(true);
        setBurnProperties(blockID, 30, 60);
    }

    public int getSubCount() {
        return subCount;
    }

    @ForgeSubscribe
    public void onBonemeal(BonemealEvent event) {
        Block bonemealedBlock = Block.blocksList[event.ID];
        if (bonemealedBlock == null || bonemealedBlock.blockID != this.blockID) {
            return;
        }
        if (JaffasTrees.bonemealingAllowed) {
            event.setResult(Event.Result.ALLOW);
            if (!event.world.isRemote /*&& JaffasFood.rand.nextFloat() < 0.30*/) {
                tryGrow(event.world, event.X, event.Y, event.Z, rand);
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random) {
        super.updateTick(world, x, y, z, par5Random);
        if (!world.isRemote) {
            if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(7) == 0)
                tryGrow(world, x, y, z, par5Random);
        }
    }

    private void tryGrow(World world, int x, int y, int z, Random rand) {
        if (world.isRemote) return;

        int metadata = world.getBlockMetadata(x, y, z);

        if (JaffasTrees.debug) {
            Log.printInfo("meta(" + metadata + ") markForDecay("
                    + BlockFruitLeaves.areLeavesMarkedForDecay(metadata) + ") setLeavesDecay("
                    + BlockFruitLeaves.setLeavesDecay(metadata) + ") areAfterSet("
                    + BlockFruitLeaves.areLeavesMarkedForDecay(BlockFruitLeaves.setLeavesDecay(metadata)) + ")");
        }

        if (!BlockFruitLeaves.areLeavesMarkedForDecay(metadata)) {
            setBlockMetadata(world, x, y, z, BlockFruitLeaves.setLeavesDecay(metadata));
            if (JaffasTrees.debug) {
                Log.printInfo("after set: " + world.getBlockMetadata(x, y, z));
            }
        } else {
            this.growTree(world, x, y, z, rand);
        }
    }

    @Override
    public Icon getIcon(int par1, int par2) {
        return Block.sapling.getBlockTextureFromSide(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {
    }

    public boolean growTree(World world, int x, int y, int z, Random random) {
        int metadata = world.getBlockMetadata(x, y, z);
        metadata = BlockFruitLeaves.getLeavesType(metadata);
        Object gen;
        int xShift = 0;
        int yShift = 0;

        world.setBlock(x, y, z, 0);

        gen = new WorldGenFruitTrees(true, 5, 0, metadata, false, JaffasTrees.leavesList.get(serialNumber).leavesID);

        if (!((WorldGenerator) gen).generate(world, random, x + xShift, y, z + yShift)) {
            setBlock(world, x, y, z, this.blockID, metadata);
            return false;
        }
        return true;
    }

    @Override
    public int damageDropped(int damage) {
        return BlockFruitLeaves.getLeavesType(damage);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < subCount; i++) par3List.add(new ItemStack(par1, 1, i));
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return Plains;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {
        return blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        Block soil = blocksList[world.getBlockId(x, y - 1, z)];
        return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) &&
                (soil != null && soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && canBlockStay(par1World, par2, par3, par4);
    }

    @Override
    public String getDefaultModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 2;
    }

    @Override
    public int getFertilizableBlockId() {
        return blockID;
    }

    @Override
    public boolean canFertilizeBlock(World world, int x, int y, int z, FertilizerType fertilizerType) {
        return true;
    }

    @Override
    public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType) {
        tryGrow(world, x, y, z, rand);
        return true;
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
        return stack.getItemDamage();
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        return world.getBlockId(x, y, z) == 0 && canBlockStay(world, x, y, z);
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
    }
}
