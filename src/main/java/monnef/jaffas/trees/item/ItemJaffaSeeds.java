/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.food.common.MfrHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import powercrystals.minefactoryreloaded.api.ReplacementBlock;

import static monnef.core.utils.BlockHelper.setBlock;

public class ItemJaffaSeeds extends ItemTrees implements IPlantable, IFactoryPlantable {
    private Block blockType;
    private Block soilBlock;

    public ItemJaffaSeeds(Block block, Block soilBlock) {
        super();
        this.blockType = block;
        this.soilBlock = soilBlock;
        if (block == null) throw new RuntimeException("Null block.");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (side != 1) {
            return false;
        } else if (player == null || (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack))) {
            if (canBePlanted(world, x, y, z, stack)) {
                setBlock(world, x, y + 1, z, this.getPlant(null, 0, 0, 0));
                --stack.stackSize;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /* y is not the plant block, but soil block! */
    public boolean canBePlanted(World w, int x, int y, int z, ItemStack stack) {
        Block soil = w.getBlock(x, y, z);
        return soil.canSustainPlant(w, x, y, z, ForgeDirection.UP, this) && w.isAirBlock(x, y + 1, z);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return blockType;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 0;
    }

    @Override
    public Item getSeed() {
        return this;
    }

    @Override
    public boolean canBePlanted(ItemStack stack, boolean forFermenting) {
        return true;
    }

    @Override
    public ReplacementBlock getPlantedBlock(World world, int x, int y, int z, ItemStack stack) {
        return MfrHelper.replacementBlockWithMeta(blockType, 0);
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        Block b = world.getBlock(x, y - 1, z);
        return world.isAirBlock(x, y, z) && (b == Blocks.grass || b == Blocks.dirt || b == Blocks.farmland);
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
        Block b = world.getBlock(x, y - 1, z);
        if (b == Blocks.grass || b == Blocks.dirt) {
            setBlock(world, x, y - 1, z, Blocks.farmland);
        }
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
    }
}
