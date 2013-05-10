/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;

import static monnef.core.utils.BlockHelper.setBlock;

public class ItemJaffaSeeds extends ItemTrees implements IPlantable, IFactoryPlantable {
    private int blockType;
    private int soilBlockID;

    public ItemJaffaSeeds(int id, int blockId, int soilBlockId) {
        super(id);
        this.blockType = blockId;
        this.soilBlockID = soilBlockId;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer par2EntityPlayer, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
        if (par7 != 1) {
            return false;
        } else if (par2EntityPlayer == null || (par2EntityPlayer.canPlayerEdit(x, y, z, par7, stack) && par2EntityPlayer.canPlayerEdit(x, y + 1, z, par7, stack))) {
            if (canBePlanted(world, x, y, z, stack)) {
                setBlock(world, x, y + 1, z, this.getPlantID(null, 0, 0, 0));
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
        int bottomBlock = w.getBlockId(x, y, z);
        Block soil = Block.blocksList[bottomBlock];

        return soil != null && soil.canSustainPlant(w, x, y, z, ForgeDirection.UP, this) && w.isAirBlock(x, y + 1, z);
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {
        return blockType;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return 0;
    }

    @Override
    public int getSeedId() {
        return itemID;
    }

    @Override
    public int getPlantedBlockId(World world, int x, int y, int z, ItemStack stack) {
        return blockType;
    }

    @Override
    public int getPlantedBlockMetadata(World world, int x, int y, int z, ItemStack stack) {
        return 0;
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        int bId = world.getBlockId(x, y - 1, z);
        return world.getBlockId(x, y, z) == 0 && (bId == Block.grass.blockID || bId == Block.dirt.blockID || bId == Block.tilledField.blockID);
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
        int bID = world.getBlockId(x, y - 1, z);
        if (bID == Block.grass.blockID || bID == Block.dirt.blockID) {
            setBlock(world, x, y - 1, z, Block.tilledField.blockID);
        }
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
    }
}
