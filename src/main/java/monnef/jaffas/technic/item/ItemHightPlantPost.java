/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.jaffas.technic.block.TileHighPlant;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.setBlock;
import static monnef.jaffas.technic.JaffasTechnic.highPlant;

public class ItemHightPlantPost extends ItemTechnic {
    public ItemHightPlantPost(int textureIndex) {
        super(textureIndex);
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        } else if (side != 1) {
            //not the top of a block
            return false;
        } else {
            Block activatedBlock = world.getBlock(x, y, z);
            boolean replacing = false;

            if (activatedBlock != null && activatedBlock.isReplaceable(world, x, y, z)) {
                replacing = true;
            } else {
                y++;
            }

            Block blockToPlace = highPlant;
            int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 2) % 4;

            if (player.canPlayerEdit(x, y, z, side, item) &&
                    player.canPlayerEdit(x, y + 1, z, side, item) &&
                    highPlant.validSoil(world, x, y - 1, z)) {
                if (world.isAirBlock(x, y, z) || replacing) {
                    if (world.isAirBlock(x, y + 1, z)) {
                        int masterMeta1 = highPlant.setDirection(0, direction);
                        int masterMeta2 = highPlant.setMaster(masterMeta1);
                        setBlock(world, x, y, z, blockToPlace, masterMeta2);

                        int slaveMeta = highPlant.setSlave(0);
                        setBlock(world, x, y + 1, z, blockToPlace, slaveMeta);

                        --item.stackSize;

                        TileHighPlant tile = (TileHighPlant) world.getTileEntity(x, y, z);
                        tile.setStructureHeight(2);
                        return true;

                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
