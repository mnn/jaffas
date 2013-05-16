/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

public class CommandJaffas extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffas";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] parameters) {

    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("jam");
    }
}
