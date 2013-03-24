package monnef.jaffas.food.item;

import monnef.core.utils.BlockHelper;
import monnef.core.utils.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.*;
import static monnef.jaffas.food.mod_jaffas_food.blockPizza;
import static monnef.jaffas.food.mod_jaffas_food.getItem;

public class ItemPizza extends ItemJaffaBase {
    public ItemPizza(int par1) {
        super(par1);
    }

    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        } else if (side != 1) {
            //not the top of a block
            return false;
        } else {
            y++;

            Block blockToPlace = blockPizza;
            int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 0) % 2;

            int meta = blockPizza.setPieces(0, 6);
            meta = blockPizza.setRotation(meta, direction == 1);

            if (player.canPlayerEdit(x, y, z, side, item) && player.canPlayerEdit(x, y + 1, z, side, item)) {
                if (world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z)) {
                    setBlock(world, x, y, z, blockToPlace.blockID, meta);
                    --item.stackSize;

                    PlayerHelper.giveItemToPlayer(player, new ItemStack(getItem(JaffaItem.cakeTin)));

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
