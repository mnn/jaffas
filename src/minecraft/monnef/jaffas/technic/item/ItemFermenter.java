/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.setBlock;
import static monnef.jaffas.technic.JaffasTechnic.fermenter;

public class ItemFermenter extends ItemTechnic {
    public ItemFermenter(int id, int texture) {
        super(id, texture);
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

            Block blockToPlace = fermenter;
            int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 1) % 4;

            if (player.canPlayerEdit(x, y, z, side, item) &&
                    player.canPlayerEdit(x, y + 1, z, side, item)) {
                if (world.isAirBlock(x, y, z) || replacing) {
                    if (world.isAirBlock(x, y + 1, z)) {
                        int masterMeta1 = fermenter.setDirection(0, direction);
                        int masterMeta2 = fermenter.setMaster(masterMeta1);
                        setBlock(world, x, y, z, blockToPlace.blockID, masterMeta2);

                        int slaveMeta = fermenter.setSlave(0);
                        setBlock(world, x, y + 1, z, blockToPlace.blockID, slaveMeta);

                        --item.stackSize;

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
