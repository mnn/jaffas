/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.command;

import monnef.core.utils.PlayerHelper;
import monnef.core.utils.ServerUtils;
import monnef.core.utils.WeatherHelper;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.common.CoolDownType;
import monnef.jaffas.food.network.HomeStonePacket;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.TileFungiBox;
import monnef.jaffas.technic.entity.EntityCombineHarvester;
import monnef.jaffas.technic.entity.EntityCombineHarvester$;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;

import static monnef.core.utils.PlayerHelper.addMessage;
import static monnef.jaffas.food.JaffasFood.cropGrowthDisabled;

public class CommandJaffasOP extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffasop";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "command.jaffasop.usage";
    }

    @Override
    public void processCommand(ICommandSender commandsender, String[] parameters) {
        if (parameters.length <= 0) return;
        else if (parameters.length == 2 && parameters[0].equals("fun_speed")) {
            int speed;

            try {
                speed = Integer.parseInt(parameters[1]);
            } catch (NumberFormatException e) {
                addMessage(commandsender, "cannot parse number");
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
                    playerName = ((EntityPlayer) commandsender).getDisplayName();
                } else {
                    addMessage(commandsender, "Cannot issue this command on a non-player.");
                    return;
                }
            } else {
                playerName = parameters[1];
            }
            EntityPlayer player = ServerUtils.getPlayerForUsername(playerName);
            if (player == null) {
                addMessage(commandsender, "Player not found.");
                return;
            }
            CoolDownRegistry.setCoolDown(player.getUniqueID(), CoolDownType.SPAWN_STONE, 1);
            HomeStonePacket.sendSyncPacket(player, false);
            addMessage(commandsender, String.format("Cooldown on home stone has been cleared for a player %s.", playerName));
        } else if (parameters.length == 1 && "crop_growth".equals(parameters[0])) {
            cropGrowthDisabled = !cropGrowthDisabled;
            addMessage(commandsender, String.format("cropGrowthDisabled: %s", cropGrowthDisabled));
        } else if (parameters.length == 1 && "comb_ha".equals(parameters[0])) {
            EntityCombineHarvester$.MODULE$.debugHarvestingAreaComputation_$eq(!EntityCombineHarvester.debugHarvestingAreaComputation());
            addMessage(commandsender, String.format("debugHarvestingAreaComputation: %s", EntityCombineHarvester.debugHarvestingAreaComputation()));
        } else {
            addMessage(commandsender, "Unknown parameters.");
        }
    }

    private void printWeatherInfo(EntityPlayer commandsender) {
        PlayerHelper.addMessage(commandsender, WeatherHelper.generateWeatherInfo(commandsender.worldObj));
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("jamop");
    }
}
