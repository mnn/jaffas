package monnef.jaffas.trees;


import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;

public class CommandFruitDebug extends CommandBase {
    @Override
    public String getCommandName() {
        return "fruitdebug";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        /*TileEntityFruitLeaves.dropChanceMultiplier = TileEntityFruitLeaves.dropChanceMultiplier == 1 ? 10 : 1;
        var1.sendChatToPlayer("mul set to " + TileEntityFruitLeaves.dropChanceMultiplier);*/
        int newTimer = 20 * 6;
        TileEntityFruitLeaves.timerMax = TileEntityFruitLeaves.timerMax == 20 * 60 ? newTimer : 20 * 60;
        var1.sendChatToPlayer("global timer max set to " + TileEntityFruitLeaves.timerMax);
    }

}
