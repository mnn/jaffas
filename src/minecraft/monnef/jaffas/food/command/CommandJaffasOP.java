/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.command;

import monnef.core.utils.WeatherHelper;
import monnef.jaffas.food.achievement.AchievementsHandler;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.common.CoolDownType;
import monnef.jaffas.food.server.SpawnStoneServerPacketSender;
import monnef.jaffas.technic.block.TileFungiBox;
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
        } else if (parameters.length == 2 && parameters[0].equals("fun_speed")) {
            int speed;

            try {
                speed = Integer.parseInt(parameters[1]);
            } catch (NumberFormatException e) {
                commandsender.sendChatToPlayer("cannot parse number");
                return;
            }

            if (speed > 0) {
                TileFungiBox.setDebugSpeedOverride(speed);
            } else {
                TileFungiBox.disableDebugSpeedOverride();
            }
        } else if (parameters.length == 1 && "wet_rain".equals(parameters[0])) {
            if (commandsender instanceof EntityPlayer) {
                WeatherHelper.toggleRain(((EntityPlayer) commandsender).worldObj);
                printWeatherInfo((EntityPlayer) commandsender);
            }
        } else if (parameters.length == 1 && "wet_thun".equals(parameters[0])) {
            if (commandsender instanceof EntityPlayer) {
                WeatherHelper.toggleThundering(((EntityPlayer) commandsender).worldObj);
                printWeatherInfo((EntityPlayer) commandsender);
            }
        } else if (parameters.length == 1 && "wet_rath".equals(parameters[0])) {
            if (commandsender instanceof EntityPlayer) {
                WeatherHelper.toggleRain(((EntityPlayer) commandsender).worldObj);
                WeatherHelper.toggleThundering(((EntityPlayer) commandsender).worldObj);
                printWeatherInfo((EntityPlayer) commandsender);
            }
        } else if (parameters.length == 1 && ("wet".equals(parameters[0]) || "wet_info".equals(parameters[0]))) {
            if (commandsender instanceof EntityPlayer) {
                printWeatherInfo((EntityPlayer) commandsender);
            }
        } else if (parameters.length >= 1 && "hs_reset".equals(parameters[0])) {
            String playerName;
            if (parameters.length == 1) {
                if (commandsender instanceof EntityPlayer) {
                    playerName = ((EntityPlayer) commandsender).getEntityName();
                } else {
                    commandsender.sendChatToPlayer("Cannot issue this command on a non-player.");
                    return;
                }
            } else {
                playerName = parameters[1];
            }
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerName);
            if (player == null) {
                commandsender.sendChatToPlayer("Player not found.");
                return;
            }
            CoolDownRegistry.setCoolDown(player.getEntityName(), CoolDownType.SPAWN_STONE, 1);
            SpawnStoneServerPacketSender.sendSyncPacket(player, false);
            commandsender.sendChatToPlayer(String.format("Cooldown on home stone has been cleared for a player %s.", playerName));
        }
    }

    private void printWeatherInfo(EntityPlayer commandsender) {
        commandsender.addChatMessage(WeatherHelper.generateWeatherInfo(commandsender.worldObj));
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("jamop");
    }
}
