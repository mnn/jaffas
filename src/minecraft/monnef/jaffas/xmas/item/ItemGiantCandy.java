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
    public ItemGiantCandy(int id, int textureIndex) {
        super(id, textureIndex);
        //setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        } else if (side != 1) {
            //top of a block
            return false;
        } else {
            y++;

            BlockCandy blockToPlace = JaffasXmas.BlockCandy;
            int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 1) % 4;

            if (player.canPlayerEdit(x, y, z, side, item) && player.canPlayerEdit(x, y + 1, z, side, item)) {
                if (world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z) && world.doesBlockHaveSolidTopSurface(x, y - 1, z)) {
                    setBlock(world, x, y, z, blockToPlace.blockID, direction);

                    if (world.getBlockId(x, y, z) == blockToPlace.blockID) {
                        setBlock(world, x, y + 1, z, blockToPlace.blockID, direction | 8);
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

    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Do NOT eat!");
    }

}

