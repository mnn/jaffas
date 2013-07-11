/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import monnef.jaffas.power.api.IPipeWrench;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WrenchHelper {
    public static boolean isPipeWrenchOrCompatible(Item item) {
        if (item instanceof IPipeWrench) return true;
        if (JaffasTechnic.omniWrenchId == item.itemID) return true;
        return false;
    }

    public static boolean isHoldingWrench(EntityPlayer player) {
        ItemStack hand = player.getHeldItem();
        if (hand != null && isPipeWrenchOrCompatible(hand.getItem())) {
            return true;
        }
        return false;
    }
}
