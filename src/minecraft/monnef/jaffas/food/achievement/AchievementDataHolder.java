/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.achievement;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.HashSet;

public class AchievementDataHolder implements IExtendedEntityProperties {
    public static final String ACHIEVEMENT_DATA_HOLDER = "jaffas_achievs_holder";
    public static final String JAFFAS_ACHIEVEMENTS_TAG = "jaffasAchievements";
    public static final String ACHIEVEMENTS_ARRAY_TAG = "achievements";

    private HashSet<Integer> data;

    public boolean hasAchievement(int id) {
        return data.contains(id);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound myCompTag = (NBTTagCompound) compound.getTag(JAFFAS_ACHIEVEMENTS_TAG);

        int[] tmp = new int[data.size()];
        int ix = 0;
        for (Integer id : data) {
            tmp[ix++] = id;
        }

        myCompTag.setIntArray(ACHIEVEMENTS_ARRAY_TAG, tmp);
        compound.setCompoundTag(JAFFAS_ACHIEVEMENTS_TAG, myCompTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey(JAFFAS_ACHIEVEMENTS_TAG)) {
            NBTTagCompound myTag = compound.getCompoundTag(JAFFAS_ACHIEVEMENTS_TAG);
            if (myTag.hasKey(ACHIEVEMENTS_ARRAY_TAG)) {
                int[] arr = myTag.getIntArray(ACHIEVEMENTS_ARRAY_TAG);
                data = new HashSet<Integer>();
                for (int item : arr) {
                    data.add(item);
                }
            }
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }
}
