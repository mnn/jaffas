/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collections;

class CombinedAchievement {
    private final String achievementId;
    public ArrayList<String> required;

    CombinedAchievement(String achievementId, String... achievementsNeeded) {
        this.achievementId = achievementId;
        required = new ArrayList<String>();

        Collections.addAll(required, achievementsNeeded);
    }

    public boolean checkPlayer(EntityPlayer player) {
        for (String achivId : required)
            if (!AchievementsHandler.hasPlayerAchievement(player, achivId)) return false;
        return true;
    }

    public String getAchievementId() {
        return achievementId;
    }
}
