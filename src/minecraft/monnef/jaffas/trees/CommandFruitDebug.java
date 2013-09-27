/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees;


import monnef.core.utils.PlayerHelper;
import monnef.jaffas.trees.block.TileFruitLeaves;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandFruitDebug extends CommandBase {
    @Override
    public String getCommandName() {
        return "fruitdebug";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "command.fruitdebug.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] var2) {
        int newTimer = 20 * 6;
        TileFruitLeaves.timerMax = TileFruitLeaves.timerMax == 20 * 60 ? newTimer : 20 * 60;
        PlayerHelper.addMessage(sender, "global timer max set to " + TileFruitLeaves.timerMax);
    }

}
