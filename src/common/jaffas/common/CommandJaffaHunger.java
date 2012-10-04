package jaffas.common;

import net.minecraft.src.*;

import java.security.Permissions;

public class CommandJaffaHunger extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffahunger";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        if(var1 instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) var1;

            player.setEntityHealth(2);
            player.addPotionEffect(new PotionEffect(Potion.hunger.getId(),5*20,50));
        }
    }

}
