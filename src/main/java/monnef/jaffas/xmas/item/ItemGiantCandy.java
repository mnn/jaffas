/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.item;

import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.block.BlockCandy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

import static monnef.core.utils.BlockHelper.setBlock;

public class ItemGiantCandy extends ItemXmas {
    public ItemGiantCandy(int textureIndex) {
        super(textureIndex);
        //setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        } else if (side != 1) {
            //top of a block
            return false;
        } else {
            y++;

            BlockCandy blockToPlace = JaffasXmas.blockCandy;
            int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 1) % 4;

            if (player.canPlayerEdit(x, y, z, side, item) && player.canPlayerEdit(x, y + 1, z, side, item)) {
                if (world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z) && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
                    setBlock(world, x, y, z, blockToPlace, direction);

                    if (world.getBlock(x, y, z) == blockToPlace) {
                        setBlock(world, x, y + 1, z, blockToPlace, direction | 8);
                    }

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
        result.add("Do NOT eat!");
    }

}

