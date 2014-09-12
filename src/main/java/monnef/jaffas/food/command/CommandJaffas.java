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
        addMessage(sender, String.format("\u00A7d%s\u00A7r version \u00A76%s\u00A7r", Reference.ModName, Reference.Version));
        addMessage(sender, String.format(" Created by \u00A7a%s\u00A7r and \u00A7a%s\u00A7r", monnef.core.Reference.MONNEF, monnef.core.Reference.TIARTYOS));
        String versionInfo = ClientTickHandler.isVersionStringReady() ? String.format(" current version is \u00A7e%s\u00A7r", ClientTickHandler.cachedVersionString) : " Unable to get/process remote version.";
        addMessage(sender, versionInfo);
        String urlString = monnef.core.Reference.URL_JAFFAS_WIKI.replaceAll("^(http://)(.*$)", "\u00A77$1\u00A77\u00A79$2\u00A7r");
        addMessage(sender, String.format(" %s", urlString));
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("jam");
    }
}
