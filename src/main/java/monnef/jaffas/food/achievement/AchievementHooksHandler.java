/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class AchievementHooksHandler {
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
        if (AchievementsHandler.craftAchievementExists(item.getItem())) {
            if (player != null) {
                AchievementsHandler.craftAchievementCompleted(item.getItem(), player);
            }
        }
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent evt) {
        onCrafting(evt.player, evt.crafting, evt.craftMatrix);
    }
}


