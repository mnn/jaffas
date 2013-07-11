/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client.common;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerBasicProcessingMachine extends GuiContainerMachine {
    public static final int ENERGY_BAR_X = 146;
    public static final int ENERGY_BAR_Y = 15;
    private static final int ENERGY_BAR_WIDTH = 18;
    private static final int ENERGY_BAR_HEIGHT = 50;
    public static final int ENERGY_BAR_INNER_HEIGHT = ENERGY_BAR_HEIGHT - 2;
    protected TileEntityBasicProcessingMachine bpMachine;
    protected static ColorHelper.IntColor topColor = new ColorHelper.IntColor(53, 180, 212);
    protected static ColorHelper.IntColor bottomColor = new ColorHelper.IntColor(25, 78, 90);

    public GuiContainerBasicProcessingMachine(InventoryPlayer inventoryPlayer, TileEntityBasicProcessingMachine tileEntity, ContainerMachine container) {
        super(inventoryPlayer, tileEntity, container);

        bpMachine = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        super.drawGuiContainerForegroundLayer(param1, param2);

        if (MonnefCorePlugin.debugEnv) {
            fontRenderer.drawString(bpMachine.processTime + " / " + bpMachine.processItemTime, 8, 16, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        if (bpMachine.processItemTime != 0) {
            int m = (bpMachine.processTime * 24) / bpMachine.processItemTime;
            drawTexturedModalRect(x + 69, y + 34, 176, 14, m + 1, 16);
        }

        drawEnergyBar();
    }

    private void drawEnergyBar() {
        int bx = x + ENERGY_BAR_X;
        int by = y + ENERGY_BAR_Y;

        drawHorizontalLine(bx, ENERGY_BAR_WIDTH - 2, by, GuiHelper.COLOR_DARK_GRAY);
        drawVerticalLine(bx, by, ENERGY_BAR_HEIGHT - 1, GuiHelper.COLOR_DARK_GRAY);

        drawHorizontalLine(bx + 1, ENERGY_BAR_WIDTH - 2, by + ENERGY_BAR_HEIGHT - 1, GuiHelper.COLOR_WHITE);
        drawVerticalLine(bx + ENERGY_BAR_WIDTH - 1, by, ENERGY_BAR_HEIGHT - 1, GuiHelper.COLOR_WHITE);

        GuiHelper.drawPixel(bx + ENERGY_BAR_WIDTH - 1, by, GuiHelper.COLOR_GRAY);
        GuiHelper.drawPixel(bx, by + ENERGY_BAR_HEIGHT - 1, GuiHelper.COLOR_GRAY);

        if (bpMachine.powerMax != 0) {
            int energyValue = (bpMachine.powerStored * ENERGY_BAR_INNER_HEIGHT) / bpMachine.powerMax;
            GuiHelper.drawGradientRectFromDown(this, bx + 1, by + 1, ENERGY_BAR_WIDTH - 2, energyValue, topColor, bottomColor, GuiHelper.EnumFillRotation.TOP_DOWN, ENERGY_BAR_INNER_HEIGHT);
        }
    }

    public void drawHorizontalLine(int x, int width, int y, int color) {
        super.drawHorizontalLine(x, x + width, y, color);
    }

    @Override
    protected void drawVerticalLine(int x, int y, int height, int color) {
        super.drawVerticalLine(x, y, y + height, color);
    }

    @Override
    protected String getBackgroundTexture() {
        return "/guipmachine.png";
    }

    @Override
    protected String getTitle() {
        return bpMachine.getMachineTitle();
    }
}
