package monnef.jaffas.food.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;

import static monnef.jaffas.food.mod_jaffas_food.Log;

public class CommandJaffaHunger extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffahunger";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        EntityPlayer player;

        if (var2.length > 0 && var2[0].length() >= 1) {
            player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(var2[0]);
            if (player == null) {
                var1.sendChatToPlayer("Player not found.");
                return;
            }
        } else if (var1 instanceof EntityPlayer) {
            player = (EntityPlayer) var1;
        } else {
            Log.printInfo("jaffahunger cannot get valid target");
            return;
        }

        player.setEntityHealth(2);
        player.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 7 * 20, 50));

    }

}
