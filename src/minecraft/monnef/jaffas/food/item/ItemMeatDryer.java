/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.setBlock;

public class ItemMeatDryer extends ItemJaffaBase {
    public ItemMeatDryer(int id) {
        super(id);
    }

    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        } else if (side != 1) {
            //not the top of a block
            return false;
        } else {
            int activatedBlockId = world.getBlockId(x, y, z);
            Block activatedBlock = Block.blocksList[activatedBlockId];
            boolean replacing = false;

            if (activatedBlock != null && activatedBlock.isBlockReplaceable(world, x, y, z)) {
                replacing = true;
            } else {
                y++;
            }

            Block blockToPlace = ContentHolder.blockMeatDryer;
            int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 3) % 4;

            if (player.canPlayerEdit(x, y, z, side, item)) {
                if (world.isAirBlock(x, y, z) || replacing) {
                    setBlock(world, x, y, z, blockToPlace.blockID, direction);

                    --item.stackSize;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
