/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.item.ItemJaffaPlate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PlateUnequipper {
    private static final int TOTAL_ARMOR_COUNT = 4;

    @ForgeSubscribe
    public void onHurt(LivingHurtEvent event) {
        if (event.entity.worldObj.isRemote) return;
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            for (int i = 0; i < TOTAL_ARMOR_COUNT; i++) {
                ItemStack armor = player.getCurrentArmor(i);
                if (armor != null && armor.getItem() instanceof ItemJaffaPlate) {
                    ItemJaffaPlate piece = (ItemJaffaPlate) armor.getItem();
                    if (piece.unequipWhenDamaged() && piece.nearlyDestroyed(armor)) {
                        player.setCurrentItemOrArmor(i + 1, null);
                        armor.damageItem(1,player);
                        PlayerHelper.giveItemToPlayer(player, armor);
                    }
                }
            }
        }
    }
}
