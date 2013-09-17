/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import monnef.core.calendar.IEventCalendarAction;
import monnef.core.utils.EventCalendarWrapper;
import monnef.core.utils.StringsHelper;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.network.AchievementPacket;
import monnef.jaffas.food.network.NetworkHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.LinkedHashSet;
import java.util.Set;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.JaffasFood.rand;

public class AchievementDataHolder implements IExtendedEntityProperties {
    public static final String ACHIEVEMENT_DATA_HOLDER = "jaffas_achievs_holder";
    public static final String JAFFAS_ACHIEVEMENTS_TAG = "jaffasAchievements";
    public static final String ACHIEVEMENTS_ARRAY_TAG = "achievements";
    public static final String HASH_TAG = "hash";

    private LinkedHashSet<Integer> data = new LinkedHashSet<Integer>();
    private EntityPlayer player;
    private boolean corruptHash = false;

    public AchievementDataHolder(EntityPlayer player) {
        this.player = player;
    }

    public boolean hasAchievement(int id) {
        return data.contains(id);
    }

    public void markAchievementCompleted(int id) {
        data.add(id);
    }

    public void markAchievementNotCompleted(int id) {
        data.remove(id);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        if (ConfigurationManager.achievementsDisabled) return;

        if (!compound.hasKey(JAFFAS_ACHIEVEMENTS_TAG)) {
            compound.setCompoundTag(JAFFAS_ACHIEVEMENTS_TAG, new NBTTagCompound());
        }
        NBTTagCompound myCompTag = (NBTTagCompound) compound.getTag(JAFFAS_ACHIEVEMENTS_TAG);

        int[] tmp = new int[data.size()];
        int ix = 0;
        for (Integer id : data) {
            tmp[ix++] = id;
        }

        myCompTag.setIntArray(ACHIEVEMENTS_ARRAY_TAG, tmp);
        myCompTag.setString(HASH_TAG, calculateHash(data));
        compound.setCompoundTag(JAFFAS_ACHIEVEMENTS_TAG, myCompTag);
    }

    private String calculateHash(LinkedHashSet<Integer> input) {
        StringBuilder sb = new StringBuilder();
        for (Integer item : input) {
            Achievement ach = AchievementsHandler.getAchievement(item);
            if (ach == null) {
                // unknown achievement => salt the hash
                sb.append(rand.nextInt());
            } else {
                sb.append(ach.toString());
            }
        }

        if (corruptHash) {
            Log.printInfo(String.format("Corrupted achievement save hash of player \"%s\".", player.username));
            sb.append(rand.nextInt());
        }

        return StringsHelper.getMD5(sb.toString());
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (ConfigurationManager.achievementsDisabled) return;

        if (compound.hasKey(JAFFAS_ACHIEVEMENTS_TAG)) {
            NBTTagCompound myTag = compound.getCompoundTag(JAFFAS_ACHIEVEMENTS_TAG);
            if (myTag.hasKey(ACHIEVEMENTS_ARRAY_TAG)) {
                int[] arr = myTag.getIntArray(ACHIEVEMENTS_ARRAY_TAG);
                data = new LinkedHashSet<Integer>();
                for (int item : arr) {
                    data.add(item);
                }

                String hash = "";
                if (myTag.hasKey(HASH_TAG)) {
                    hash = myTag.getString(HASH_TAG);
                }
                String currentHash = calculateHash(data);

                if (!currentHash.equals(hash)) {
                    String message = String.format("Player \"%s\" has corrupted achievements save, purging.", player.username);
                    Log.printWarning(message);
                    EventCalendarWrapper.addAction(20, new DelayedAchievementsRemover(player, data, message));
                    data = new LinkedHashSet<Integer>();
                }
            }
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public void sendSyncPackets() {
        for (Integer item : data) {
            NetworkHelper.sendToClient((new AchievementPacket(item)).makePacket(), player);
        }
    }

    public void recreateAchievements() {
        for (Integer item : data) {
            AchievementsHandler.completeAchievement(item, player);
        }
    }

    public void corrupt() {
        this.corruptHash = true;
    }

    private static class DelayedAchievementsRemover implements IEventCalendarAction {
        private EntityPlayer player;
        private Set<Integer> data;
        private String message;

        private DelayedAchievementsRemover(EntityPlayer player, Set<Integer> data, String message) {
            this.player = player;
            this.data = data;
            this.message = message;
        }

        @Override
        public void execute() {
            for (int item : data) {
                Achievement ach = AchievementsHandler.getAchievement(item);
                if (ach != null) {
                    AchievementsHandler.removeAchievement(ach, player);
                }
            }
            player.addChatMessage(message);
        }
    }
}
