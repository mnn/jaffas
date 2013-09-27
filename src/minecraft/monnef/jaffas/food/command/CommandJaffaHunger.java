/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;

import static monnef.core.utils.PlayerHelper.addMessage;
import static monnef.jaffas.food.JaffasFood.Log;

public class CommandJaffaHunger extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffahunger";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] var2) {
        EntityPlayer player;

        if (var2.length > 0 && var2[0].length() >= 1) {
            player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(var2[0]);
            if (player == null) {
                addMessage(sender, "Player not found.");
                return;
            }
        } else if (sender instanceof EntityPlayer) {
            player = (EntityPlayer) sender;
        } else {
            Log.printInfo("jaffahunger cannot get valid target");
            return;
        }

        player.setHealth(2);
        player.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 7 * 20, 50));
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "command.jaffahunger.usage";
    }
}
