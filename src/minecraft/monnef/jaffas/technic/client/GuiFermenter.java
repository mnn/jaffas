/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.core.utils.ColorHelper;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.technic.block.ContainerFermenter;
import monnef.jaffas.technic.block.TileEntityFermenter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

import static monnef.core.utils.GuiHelper.EnumFillRotation.LEFT_RIGHT;
import static monnef.core.utils.GuiHelper.EnumFillRotation.TOP_DOWN;
import static monnef.core.utils.ColorHelper.IntColor;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.BEER;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.BEER_RAW;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.WINE;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.WINE_RAW;

public class GuiFermenter extends GuiContainer {
    private static final String GUI_TEXTURE = "/guifermenter.png";
    public static final int TANK_POS_X = 24;
    public static final int TANK_POS_Y = 20;
    public static final int TANK_WIDTH = 16;
    public static final int WORK_WIDTH = 4;
    public static final int WORK_X = 14;
    public static int TANK_HEIGHT = 46;

    public TileEntityFermenter tile;

    private static HashMap<FermentedLiquid, ColorPair> liquidToColors;
    private static IntColor WORK_BOTTOM_COLOR = new IntColor(255, 150, 150);
    private static IntColor WORK_TOP_COLOR = new IntColor(255, 100, 100);

    static {
        liquidToColors = new HashMap<FermentedLiquid, ColorPair>();

        addLiquidToColorsMapping(BEER, new IntColor(220, 220, 90));
        addLiquidToColorsMapping(BEER_RAW, new IntColor(225, 175, 70));

        addLiquidToColorsMapping(WINE_RAW, new IntColor(200, 70, 125));
        addLiquidToColorsMapping(WINE, new IntColor(190, 35, 90));
    }

    private static void addLiquidToColorsMapping(FermentedLiquid liquid, IntColor color) {
        int second = ColorHelper.addBrightness(color.toInt(), -33);
        liquidToColors.put(liquid, new ColorPair(color, ColorHelper.getColor(second)));
    }

    private static class ColorPair {
        public IntColor first;
        public IntColor second;

        private ColorPair(IntColor first, IntColor second) {
            this.first = first;
            this.second = second;
        }
    }

    public GuiFermenter(InventoryPlayer inventoryPlayer,
                        TileEntityFermenter tileEntity) {
        super(new ContainerFermenter(inventoryPlayer, tileEntity));
        tile = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Fermenter", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (tile.isWorking()) {
            int m = (tile.getWorkMeter() * TANK_HEIGHT) / tile.getMaxWorkMeter();
            GuiHelper.drawGradientRectFromDown(this, x + WORK_X, y + TANK_POS_Y, WORK_WIDTH, m, WORK_BOTTOM_COLOR, WORK_TOP_COLOR, TOP_DOWN, TANK_HEIGHT);
        }

        if (!tile.isEmpty()) {
            int m = (tile.getLiquidAmount() * TANK_HEIGHT) / TileEntityFermenter.FERMENTER_CAPACITY;
            //DrawingHelper.drawRect(x + 24, y + 20, 16, m, liquidToColors.get(tile.getLiquid()));
            ColorPair pair = liquidToColors.get(tile.getLiquid());
            GuiHelper.drawGradientRectFromDown(this, x + TANK_POS_X, y + TANK_POS_Y, TANK_WIDTH, m, pair.first, pair.second, LEFT_RIGHT, TANK_HEIGHT);
        }
    }
    // x, y, u, v, width, height
}
