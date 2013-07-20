/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client.common;

import monnef.core.utils.ColorHelper;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainerMachine extends GuiContainer {
    public static final int ENERGY_BAR_X = 146;
    public static final int ENERGY_BAR_Y = 15;
    private static final int ENERGY_BAR_HEIGHT = 50;
    public static final int ENERGY_BAR_INNER_HEIGHT = ENERGY_BAR_HEIGHT - 2;
    private static final int ENERGY_BAR_WIDTH = 18;
    protected static ColorHelper.IntColor topColor = new ColorHelper.IntColor(53, 180, 212);
    protected static ColorHelper.IntColor bottomColor = new ColorHelper.IntColor(25, 78, 90);
    protected final TileEntityMachineWithInventory tile;

    public GuiContainerMachine(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory tileEntity, ContainerMachine container) {
        super(container);

        this.tile = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color
        fontRenderer.drawString(getTitle(), 8, 6, 4210752);
        //draws "Inventory" or your regional equivalent
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    protected abstract String getTitle();

    protected int x, y;

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(getBackgroundTexture());
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (tile.isPowerBarRenderingEnabled())
            drawEnergyBar(tile);
    }

    protected abstract String getBackgroundTexture();

    protected void drawEnergyBar(TileEntityMachineWithInventory tile) {
        int bx = x + ENERGY_BAR_X;
        int by = y + ENERGY_BAR_Y;

        drawHorizontalLine(bx, ENERGY_BAR_WIDTH - 2, by, GuiHelper.COLOR_DARK_GRAY);
        drawVerticalLine(bx, by, ENERGY_BAR_HEIGHT - 1, GuiHelper.COLOR_DARK_GRAY);

        drawHorizontalLine(bx + 1, ENERGY_BAR_WIDTH - 2, by + ENERGY_BAR_HEIGHT - 1, GuiHelper.COLOR_WHITE);
        drawVerticalLine(bx + ENERGY_BAR_WIDTH - 1, by, ENERGY_BAR_HEIGHT - 1, GuiHelper.COLOR_WHITE);

        GuiHelper.drawPixel(bx + ENERGY_BAR_WIDTH - 1, by, GuiHelper.COLOR_GRAY);
        GuiHelper.drawPixel(bx, by + ENERGY_BAR_HEIGHT - 1, GuiHelper.COLOR_GRAY);

        if (tile.powerMax != 0) {
            int energyValue = (tile.powerStored * ENERGY_BAR_INNER_HEIGHT) / tile.powerMax;
            GuiHelper.drawGradientRectFromDown(this, bx + 1, by + 1, ENERGY_BAR_WIDTH - 2, energyValue, topColor, bottomColor, GuiHelper.EnumFillRotation.TOP_DOWN, ENERGY_BAR_INNER_HEIGHT);
        }

        GL11.glColor4f(1, 1, 1, 1);
    }

    @Override
    public void drawHorizontalLine(int x, int width, int y, int color) {
        super.drawHorizontalLine(x, x + width, y, color);
    }

    @Override
    protected void drawVerticalLine(int x, int y, int height, int color) {
        super.drawVerticalLine(x, y, y + height, color);
    }
}
