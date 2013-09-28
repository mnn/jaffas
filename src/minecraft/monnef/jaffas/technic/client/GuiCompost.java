/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.core.client.GuiContainerJaffas;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.technic.block.ContainerCompost;
import monnef.jaffas.technic.block.TileCompostCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiCompost extends GuiContainerJaffas {
    public static final int TANK_METER_HEIGHT_MAX = 46;

    public TileCompostCore core;

    public GuiCompost(InventoryPlayer inventoryPlayer,
                      TileCompostCore tileEntity) {
        super(new ContainerCompost(inventoryPlayer, tileEntity));
        core = tileEntity;
        setBackgroundTexture("guicompost.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Composting tank", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        int n = (TANK_METER_HEIGHT_MAX * core.getTankMeter()) / core.getMaxTankValue();
        GuiHelper.drawModalRectFromDown(this, x + 76, y + 20, 176, 31, 16, n, TANK_METER_HEIGHT_MAX);

        // 176 77 - 191 122, scale
        drawTexturedModalRect(x + 76, y + 20, 176, 77, 16, TANK_METER_HEIGHT_MAX);

        int m = (core.getWorkMeter() * 24) / core.getMaxWork();
        drawTexturedModalRect(x + 100, y + 34, 176, 14, m + 1, 16);
    }
    // x, y, u, v, width, height

    @Override
    public List<String> fillTooltips(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        GuiCompost compostGui = (GuiCompost) gui;
        if (GuiHelper.isMouseInRect(gui, mousex, mousey, 76, 20, 16, GuiCompost.TANK_METER_HEIGHT_MAX)) {
            currenttip.add(String.format("§2Compost §8(§7%d§8/§7%d§8)§r", compostGui.core.getTankMeter(), compostGui.core.getMaxTankValue()));
        } else if (GuiHelper.isMouseInRect(gui, mousex, mousey, 102, 32, 22, 17)) {
            currenttip.add(String.format("§7%d%%§r", (compostGui.core.getWorkMeter() * 100) / compostGui.core.getMaxWork()));
        }
        return currenttip;
    }
}
