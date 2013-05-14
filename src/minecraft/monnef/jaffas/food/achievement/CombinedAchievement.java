/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

class CombinedAchievement {
    public ArrayList<Integer> required;

    CombinedAchievement(Integer[] achievementsNeeded) {
        required = new ArrayList<Integer>();

        for (Integer a : achievementsNeeded) {
            required.add(a);
        }
    }

    // TODO achievements
    /*
    public boolean checkPlayer(EntityPlayer player) {
        if (player == null) return false;

        int[] array =
        boolean[] found = new boolean[required.size()];
        for (int playersAchievements : array) {
            int index = required.indexOf(playersAchievements);
            if (index != -1) {
                found[index] = true;
            }
        }

        for (int i = 0; i < found.length; i++) {
            if (!found[i]) return false;
        }


        // all required items are here, we're ok
        return true;

        return false;
    }
    */
}
