package monnef.jaffas.food.command;

import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.client.RenderCombineHarvester;
import monnef.jaffas.technic.entity.EntityCombineHarvester;
import monnef.jaffas.technic.entity.EntityCombineHarvester$;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

import static monnef.core.utils.PlayerHelper.addMessage;

public class CommandJaffasClient extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffasclient";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender commandsender, String[] parameters) {
        if (parameters.length == 4 && "comb_wheels_rp".equals(parameters[0])) {
            try {
                float x = Float.parseFloat(parameters[1]);
                float y = Float.parseFloat(parameters[2]);
                float z = Float.parseFloat(parameters[3]);
                JaffasTechnic.proxy.setCombineWheelsRotationPoint(x, y, z);
            } catch (NumberFormatException e) {
                addMessage(commandsender, "cannot parse number");
            }
        } else if (parameters.length == 4 && "comb_reel_rp".equals(parameters[0])) {
            try {
                float x = Float.parseFloat(parameters[1]);
                float y = Float.parseFloat(parameters[2]);
                float z = Float.parseFloat(parameters[3]);
                JaffasTechnic.proxy.setCombineReelRotationPoint(x, y, z);
            } catch (NumberFormatException e) {
                addMessage(commandsender, "cannot parse number");
            }
        } else if (parameters.length == 4 && "comb_cp".equals(parameters[0])) {
            try {
                float x = Float.parseFloat(parameters[1]);
                float y = Float.parseFloat(parameters[2]);
                float z = Float.parseFloat(parameters[3]);
                RenderCombineHarvester.CenterPoint$.MODULE$.x_$eq(x);
                RenderCombineHarvester.CenterPoint$.MODULE$.y_$eq(y);
                RenderCombineHarvester.CenterPoint$.MODULE$.z_$eq(z);
            } catch (NumberFormatException e) {
                addMessage(commandsender, "cannot parse number");
            }
        } else if (parameters.length == 2 && "comb_chd".equals(parameters[0])) {
            try {
                float r = Float.parseFloat(parameters[1]);
                EntityCombineHarvester$.MODULE$.CHAIR_FROM_CENTER_DISTANCE_$eq(r);
            } catch (NumberFormatException e) {
                addMessage(commandsender, "cannot parse number");
            }
        } else if (parameters.length == 4 && "comb_chp".equals(parameters[0])) {
            try {
                float x = Float.parseFloat(parameters[1]);
                float y = Float.parseFloat(parameters[2]);
                float z = Float.parseFloat(parameters[3]);
                EntityCombineHarvester.ChairPoint$.MODULE$.x_$eq(x);
                EntityCombineHarvester.ChairPoint$.MODULE$.y_$eq(y);
                EntityCombineHarvester.ChairPoint$.MODULE$.z_$eq(z);
            } catch (NumberFormatException e) {
                addMessage(commandsender, "cannot parse number");
            }
        } else {
            addMessage(commandsender, "Unknown parameters.");
        }
    }

    public List getCommandAliases() {
        return Arrays.asList("jamcl");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
