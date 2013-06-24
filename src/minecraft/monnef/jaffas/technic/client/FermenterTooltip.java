/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import codechicken.nei.forge.IContainerTooltipHandler;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.technic.block.TileEntityFermenter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FermenterTooltip implements IContainerTooltipHandler {
    @Override
    public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        if (!(gui instanceof GuiFermenter)) return currenttip;
        GuiFermenter fermenterGui = (GuiFermenter) gui;
        if (GuiHelper.isMouseInRect(fermenterGui, mousex, mousey, GuiFermenter.TANK_POS_X, GuiFermenter.TANK_POS_Y, GuiFermenter.TANK_WIDTH, GuiFermenter.TANK_HEIGHT)) {
            TileEntityFermenter tile = fermenterGui.tile;
            currenttip.add(String.format("§2%s §8(§7%d§8/§7%d§8)§r", tile.getLiquid().getCapTitle(), tile.getLiquidAmount(), TileEntityFermenter.FERMENTER_CAPACITY));
        } else if (GuiHelper.isMouseInRect(fermenterGui, mousex, mousey, GuiFermenter.WORK_X - 1, GuiFermenter.TANK_POS_Y, GuiFermenter.WORK_WIDTH + 1, GuiFermenter.TANK_HEIGHT)) {
            TileEntityFermenter tile = fermenterGui.tile;
            String percent = tile.getMaxWorkMeter() == 0 ? "?" : String.format("%d", (tile.getWorkMeter() * 100) / tile.getMaxWorkMeter());
            currenttip.add(String.format("§7%s%%§r", percent));
        }
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
        return currenttip;
    }
}
