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
import net.minecraftforge.event.entity.EntityEvent;

public class AchievementHooksHandler {
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
        if (AchievementsHandler.craftAchievementExists(item.itemID)) {
            if (player != null) {
                AchievementsHandler.craftAchievementCompleted(item.itemID, player);
            }
        }
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent evt) {
        onCrafting(evt.player, evt.crafting, evt.craftMatrix);
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing evt) {
        if (evt.entity instanceof EntityPlayer) {
            evt.entity.registerExtendedProperties(AchievementDataHolder.ACHIEVEMENT_DATA_HOLDER, new AchievementDataHolder((EntityPlayer) evt.entity));
        }
    }
}


