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

import static monnef.core.utils.PlayerHelper.addMessage;

public class CommandJaffas extends CommandBase {
    @Override
    public String getCommandName() {
        return "jaffas";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "command.jaffas.usage";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] parameters) {
        showAbout(icommandsender);
    }

    private void showAbout(ICommandSender sender) {
        addMessage(sender, String.format("§d%s§r version §6%s§r", Reference.ModName, Reference.Version));
        addMessage(sender, String.format(" Created by §a%s§r and §a%s§r", monnef.core.Reference.MONNEF, monnef.core.Reference.TIARTYOS));
        String versionInfo = ClientTickHandler.isVersionStringReady() ? String.format(" current version is §e%s§r", ClientTickHandler.cachedVersionString) : " Unable to get/process remote version.";
        addMessage(sender, versionInfo);
        String urlString = monnef.core.Reference.URL_JAFFAS_WIKI.replaceAll("^(http://)(.*$)", "§7$1§7§9$2§r");
        addMessage(sender, String.format(" %s", urlString));
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("jam");
    }
}
