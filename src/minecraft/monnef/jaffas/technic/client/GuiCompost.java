/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerTooltipHandler;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.technic.block.ContainerCompost;
import monnef.jaffas.technic.block.TileEntityCompostCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiCompost extends GuiContainer {
    public static final String GUI_TEXTURE = "/guicompost.png";
    public static final int TANK_METER_HEIGHT_MAX = 46;

    public TileEntityCompostCore core;

    static {
        GuiContainerManager.addTooltipHandler(new TankTooltip());
    }

    public GuiCompost(InventoryPlayer inventoryPlayer,
                      TileEntityCompostCore tileEntity) {
        super(new ContainerCompost(inventoryPlayer, tileEntity));
        core = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Compost", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        //draw your Gui here, only thing you need to change is the path
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int n = (TANK_METER_HEIGHT_MAX * core.getTankMeter()) / core.getMaxTankValue();
        GuiHelper.drawModalRectFromDown(this, x + 76, y + 20, 176, 31, 16, n, TANK_METER_HEIGHT_MAX);

        // 176 77 - 191 122, scale
        drawTexturedModalRect(x + 76, y + 20, 176, 77, 16, TANK_METER_HEIGHT_MAX);

        int m = (core.getWorkMeter() * 24) / core.getMaxWork();
        drawTexturedModalRect(x + 100, y + 34, 176, 14, m + 1, 16);
    }
    // x, y, u, v, width, height

    private static class TankTooltip implements IContainerTooltipHandler {
        @Override
        public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
            if (!(gui instanceof GuiCompost)) return currenttip;
            if (GuiHelper.isMouseInRect(gui, mousex, mousey, 76, 20, 16, TANK_METER_HEIGHT_MAX)) {
                GuiCompost compostGui = (GuiCompost) gui;
                currenttip.add(String.format("Tank: %d/%d", compostGui.core.getTankMeter(), compostGui.core.getMaxTankValue()));
            }
            return currenttip;
        }

        @Override
        public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
            return currenttip;
        }
    }
}
