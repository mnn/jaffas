/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

import static monnef.core.utils.BlockHelper.setBlock;

public class ItemSink extends ItemJaffaBase {
    public ItemSink(int id) {
        super(id);
    }

    public ItemSink(int id, int textureIndex) {
        super(id);
        //setIconIndex(textureIndex);
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        } else if (side != 1) {
            //not the top of a block
            return false;
        } else {
            y++;

            Block blockToPlace = ContentHolder.blockSink;
            int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 3) % 4;

            if (player.canPlayerEdit(x, y, z, side, item) && player.canPlayerEdit(x, y + 1, z, side, item)) {
                if (world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z)) {
                    setBlock(world, x, y, z, blockToPlace, direction);

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

    @Override
    public void addInformationCustom(ItemStack stack, EntityPlayer player, List<String> result, boolean par4) {
        super.addInformationCustom(stack, player, result, par4);
        result.add("Construction kit");
    }
}

