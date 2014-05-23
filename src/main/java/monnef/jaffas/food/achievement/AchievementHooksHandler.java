/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import cpw.mods.fml.common.ICraftingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent;

public class AchievementHooksHandler implements ICraftingHandler {
    @Override
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
        if (AchievementsHandler.craftAchievementExists(item.itemID)) {
            if (player != null) {
                AchievementsHandler.craftAchievementCompleted(item.itemID, player);
            }
        }
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
    }

    @ForgeSubscribe
    public void onEntityConstructing(EntityEvent.EntityConstructing evt) {
        if (evt.entity instanceof EntityPlayer) {
            evt.entity.registerExtendedProperties(AchievementDataHolder.ACHIEVEMENT_DATA_HOLDER, new AchievementDataHolder((EntityPlayer) evt.entity));
        }
    }
}


