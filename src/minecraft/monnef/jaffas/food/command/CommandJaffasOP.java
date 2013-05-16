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

import java.util.Arrays;
import java.util.List;

public class CommandJaffasOP extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffasop";
    }

    @Override
    public void processCommand(ICommandSender commandsender, String[] parameters) {
        if (parameters.length <= 0) return;
        else if (parameters.length == 2 && parameters[0].equals("ach_removeall") && parameters[1].length() > 0) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(parameters[1]);
            if (player == null) {
                commandsender.sendChatToPlayer("Player not found.");
                return;
            }
            AchievementsHandler.removeAllJaffasAchievements(player);
            commandsender.sendChatToPlayer(String.format("Achievements of \"%s\" has been reset.", player.username));
        } else if (parameters.length == 2 && parameters[0].equals("ach_corrupt") && parameters[1].length() > 0) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(parameters[1]);
            if (player == null) {
                commandsender.sendChatToPlayer("Player not found.");
                return;
            }
            AchievementsHandler.corrupt(player);
            commandsender.sendChatToPlayer(String.format("Achievements of \"%s\" has been corrupted.", player.username));
        }

    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("jamop");
    }
}
