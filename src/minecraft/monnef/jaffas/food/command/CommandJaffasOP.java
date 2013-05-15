/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.command;

import monnef.jaffas.food.achievement.AchievementsHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandJaffasOP extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffasop";
    }

    @Override
    public void processCommand(ICommandSender commandsender, String[] parameters) {
        if (parameters.length <= 0) return;
        if (parameters.length == 2 && parameters[0].equals("ach_removeall") && parameters[1].length() > 0) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(parameters[1]);
            if (player == null) {
                commandsender.sendChatToPlayer("Player not found.");
                return;
            }
            AchievementsHandler.removeAllJaffasAchievements(player);
        }
    }
}
