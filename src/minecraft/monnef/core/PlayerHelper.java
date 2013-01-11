package monnef.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerHelper {
    public static void damageCurrentItem(EntityPlayer player) {
        ItemStack handItem = player.getCurrentEquippedItem();
        handItem.damageItem(1, player);
    }
}