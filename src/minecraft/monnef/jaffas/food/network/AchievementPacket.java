/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.relauncher.Side;
import monnef.core.MonnefCorePlugin;
import monnef.jaffas.food.achievement.AchievementsHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;

import java.net.ProtocolException;

public class AchievementPacket extends JaffasPacket {
    private int achievementId;

    public AchievementPacket() {
    }

    public AchievementPacket(int achievementId) {
        this.achievementId = achievementId;
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(achievementId);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        achievementId = in.readInt();
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side != Side.CLIENT) throw new ProtocolException("wrong side");

        if (MonnefCorePlugin.debugEnv) {
            Achievement ach = AchievementsHandler.getAchievement(achievementId);
            String achStr = ach != null ? ach.getName() : "NULL";
            player.addChatMessage("Received <achievement completed #" + achievementId + ">, achH's record: " + achStr);
        }

        AchievementsHandler.completeAchievement(achievementId, player);
    }
}
