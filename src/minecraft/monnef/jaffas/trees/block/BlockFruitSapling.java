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

import java.util.List;
import java.util.Random;

import static monnef.core.utils.BlockHelper.setBlock;
import static monnef.core.utils.BlockHelper.setBlockMetadata;
import static monnef.jaffas.food.JaffasFood.Log;
import static net.minecraftforge.common.EnumPlantType.Plains;

public class BlockFruitSapling extends BlockJaffas implements IPlantable {
    public static Random rand = new Random();
    public int serialNumber = -1;

    @ForgeSubscribe
    public void onBonemeal(BonemealEvent event) {
        if (!(Block.blocksList[event.ID] instanceof BlockFruitSapling)) {
            return;
        }
        if (JaffasTrees.bonemealingAllowed) {
            event.setResult(Event.Result.ALLOW);
            if (!event.world.isRemote) {
                growTree(event.world, event.X, event.Y, event.Z, rand);
            }
        }
    }

    public BlockFruitSapling(int blockId, int blockIndexInTexture) {
        super(blockId, blockIndexInTexture, Material.plants);
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        setCreativeTab(JaffasTrees.CreativeTab);
        this.setTickRandomly(true);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (!par1World.isRemote) {
            super.updateTick(par1World, par2, par3, par4, par5Random);

            if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 && par5Random.nextInt(7) == 0) {
                int metadata = par1World.getBlockMetadata(par2, par3, par4);

                if (JaffasTrees.debug) {
                    Log.printInfo("meta(" + metadata + ") markForDecay("
                            + BlockFruitLeaves.areLeavesMarkedForDecay(metadata) + ") setLeavesDecay("
                            + BlockFruitLeaves.setLeavesDecay(metadata) + ") areAfterSet("
                            + BlockFruitLeaves.areLeavesMarkedForDecay(BlockFruitLeaves.setLeavesDecay(metadata)) + ")");
                }

                if (!BlockFruitLeaves.areLeavesMarkedForDecay(metadata)) {
                    setBlockMetadata(par1World, par2, par3, par4, BlockFruitLeaves.setLeavesDecay(metadata));
                    if (JaffasTrees.debug) {
                        Log.printInfo("after set: " + par1World.getBlockMetadata(par2, par3, par4));
                    }
                } else {
                    this.growTree(par1World, par2, par3, par4, par5Random);
                }
            }
        }
    }

    @Override
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
        return Block.sapling.getBlockTextureFromSide(1);
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
    }

    public void growTree(World par1World, int par2, int par3, int par4, Random par5Random) {
        int metadata = par1World.getBlockMetadata(par2, par3, par4);
        metadata = BlockFruitLeaves.getLeavesType(metadata);
        Object var7 = null;
        int var8 = 0;
        int var9 = 0;

        par1World.setBlock(par2, par3, par4, 0);

        var7 = new WorldGenFruitTrees(true, 5, 0, metadata, false, JaffasTrees.leavesList.get(serialNumber).leavesID);

        if (!((WorldGenerator) var7).generate(par1World, par5Random, par2 + var8, par3, par4 + var9)) {
            setBlock(par1World, par2, par3, par4, this.blockID, metadata);
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
    @Override
    public int damageDropped(int par1) {
        return BlockFruitLeaves.getLeavesType(par1);
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
/*        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));*/
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
    public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
        Block soil = blocksList[par1World.getBlockId(par2, par3 - 1, par4)];
        return (par1World.getFullBlockLightValue(par2, par3, par4) >= 8 || par1World.canBlockSeeTheSky(par2, par3, par4)) &&
                (soil != null && soil.canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this));
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
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 2;
    }
}
