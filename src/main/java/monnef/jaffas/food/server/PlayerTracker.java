/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.server;

import cpw.mods.fml.common.IPlayerTracker;
import monnef.jaffas.food.achievement.AchievementsHandler;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerTracker implements IPlayerTracker {
    @Override
    public void onPlayerLogin(EntityPlayer player) {
        SpawnStoneServerPacketSender.sendSyncPacket(player, false);
        AchievementsHandler.synchronizeAchievements(player);
    }
}
