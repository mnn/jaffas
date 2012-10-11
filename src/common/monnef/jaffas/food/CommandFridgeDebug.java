package monnef.jaffas.food;

import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;

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
