package monnef.jaffas.food.item;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

import static monnef.core.utils.BlockHelper.*;

public class ItemSink extends ItemJaffaBase {
    public ItemSink(int id) {
        super(id);
    }

    public ItemSink(int id, int textureIndex) {
        super(id);
        //setIconIndex(textureIndex);
    }

    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        } else if (side != 1) {
            //not the top of a block
            return false;
        } else {
            y++;

            Block blockToPlace = mod_jaffas_food.blockSink;
            int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 3) % 4;

            if (player.canPlayerEdit(x, y, z, side, item) && player.canPlayerEdit(x, y + 1, z, side, item)) {
                if (world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z)) {
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

    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Construction kit");
    }
}

