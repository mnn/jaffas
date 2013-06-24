/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import codechicken.nei.forge.IContainerTooltipHandler;
import monnef.core.utils.GuiHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CompostTooltip implements IContainerTooltipHandler {
    @Override
    public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        if (!(gui instanceof GuiCompost)) return currenttip;
        GuiCompost compostGui = (GuiCompost) gui;
        if (GuiHelper.isMouseInRect(gui, mousex, mousey, 76, 20, 16, GuiCompost.TANK_METER_HEIGHT_MAX)) {
            currenttip.add(String.format("§2Compost §8(§7%d§8/§7%d§8)§r", compostGui.core.getTankMeter(), compostGui.core.getMaxTankValue()));
        } else if (GuiHelper.isMouseInRect(gui, mousex, mousey, 102, 32, 22, 17)) {
            currenttip.add(String.format("§7%d%%§r", (compostGui.core.getWorkMeter() * 100) / compostGui.core.getMaxWork()));
        }
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
        return currenttip;
    }
}
