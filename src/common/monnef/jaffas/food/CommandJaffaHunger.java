package monnef.jaffas.food;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

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
            if (player == null){
                var1.sendChatToPlayer("Player not found.");
                return;
            }
        }else if (var1 instanceof EntityPlayer) {
            player = (EntityPlayer) var1;
        }else{
            System.out.println("jaffahunger cannot get valid target");
            return;
        }

        player.setEntityHealth(2);
        player.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 5 * 20, 50));

    }

}
