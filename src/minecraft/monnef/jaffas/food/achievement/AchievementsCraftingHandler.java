/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import cpw.mods.fml.common.ICraftingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class AchievementsCraftingHandler implements ICraftingHandler {

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
        Achievement achiev = AchievementsHandler.craftAchievement.get(item.itemID);
        if (achiev != null) {
            if (player != null) {
                player.addStat(achiev, 1);
            }
        }
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
    }
}


