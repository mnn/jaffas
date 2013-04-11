/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.trees.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

import static monnef.core.utils.BlockHelper.setBlock;

public class ItemJaffaSeeds extends ItemTrees implements IPlantable {
    private int blockType;
    private int soilBlockID;

    public ItemJaffaSeeds(int id, int blockId, int soilBlockId) {
        super(id);
        this.blockType = blockId;
        this.soilBlockID = soilBlockId;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par7 != 1) {
            return false;
        } else if (par2EntityPlayer == null || (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack))) {
            int var11 = par3World.getBlockId(par4, par5, par6);
            Block soil = Block.blocksList[var11];

            if (soil != null && soil.canSustainPlant(par3World, par4, par5, par6, ForgeDirection.UP, this) && par3World.isAirBlock(par4, par5 + 1, par6)) {
                setBlock(par3World, par4, par5 + 1, par6, this.getPlantID(null, 0, 0, 0));
                --par1ItemStack.stackSize;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
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
}
