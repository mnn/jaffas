/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collections;

class CombinedAchievement {
    private final int achievementId;
    public ArrayList<Integer> required;

    CombinedAchievement(int achievementId, Integer... achievementsNeeded) {
        this.achievementId = achievementId;
        required = new ArrayList<Integer>();

        Collections.addAll(required, achievementsNeeded);
    }

    public boolean checkPlayer(EntityPlayer player) {
        for (int achivId : required)
            if (!AchievementsHandler.hasPlayerAchievement(player, achivId)) return false;
        return true;
    }

    public int getAchievementId() {
        return achievementId;
    }
}
