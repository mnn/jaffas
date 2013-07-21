/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.command;

import monnef.jaffas.food.client.ClientTickHandler;
import monnef.jaffas.food.common.Reference;
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
        showAbout(icommandsender);
    }

    private void showAbout(ICommandSender sender) {
        sender.sendChatToPlayer(String.format("§d%s§r version §6%s§r", Reference.ModName, Reference.Version));
        sender.sendChatToPlayer(String.format(" Created by §a%s§r and §a%s§r", monnef.core.Reference.MONNEF, monnef.core.Reference.TIARTYOS));
        String versionInfo = ClientTickHandler.isVersionStringReady() ? String.format(" current version is §e%s§r", ClientTickHandler.cachedVersionString) : " Unable to get/process remote version.";
        sender.sendChatToPlayer(versionInfo);
        String urlString = monnef.core.Reference.URL_JAFFAS_WIKI.replaceAll("^(http://)(.*$)", "§7$1§7§9$2§r");
        sender.sendChatToPlayer(String.format(" %s", urlString));
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("jam");
    }
}
