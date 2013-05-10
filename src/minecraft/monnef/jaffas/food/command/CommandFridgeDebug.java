/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.command;

import monnef.jaffas.food.block.TileEntityFridge;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandFridgeDebug extends CommandBase {
    @Override
    public String getCommandName() {
        return "fridgedebug";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        TileEntityFridge.tickDivider = TileEntityFridge.tickDivider == 1 ? 20 : 1;
    }

}
