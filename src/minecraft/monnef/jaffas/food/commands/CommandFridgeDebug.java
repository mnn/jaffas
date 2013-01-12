package monnef.jaffas.food.commands;

import monnef.jaffas.food.blocks.TileEntityFridge;
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
