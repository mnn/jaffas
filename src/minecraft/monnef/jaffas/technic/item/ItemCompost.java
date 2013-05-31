package monnef.jaffas.technic.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCompost extends ItemTechnic {
    public ItemCompost(int id, int textureIndex) {
        super(id, textureIndex);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }

        if (ItemDye.applyBonemeal(stack, world, x, y, z, player)) {
            if (!world.isRemote) {
                world.playAuxSFX(2005, x, y, z, 0);
            }

            return true;
        }

        return false;
    }
}
