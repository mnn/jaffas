/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.core.client.GuiContainerJaffas;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.technic.block.ContainerFermenter;
import monnef.jaffas.technic.block.TileFermenter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.List;

import static monnef.core.utils.ColorHelper.IntColor;
import static monnef.core.utils.GuiHelper.EnumFillRotation.LEFT_RIGHT;
import static monnef.core.utils.GuiHelper.EnumFillRotation.TOP_DOWN;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid.BEER;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid.BEER_RAW;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid.WINE;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid.WINE_RAW;

public class GuiFermenter extends GuiContainerJaffas {
    public static final int TANK_POS_X = 24;
    public static final int TANK_POS_Y = 20;
    public static final int TANK_WIDTH = 16;
    public static final int WORK_WIDTH = 4;
    public static final int WORK_X = 14;
    public static int TANK_HEIGHT = 46;

    public TileFermenter tile;

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
                        TileFermenter tileEntity) {
        super(new ContainerFermenter(inventoryPlayer, tileEntity));
        tile = tileEntity;
        setBackgroundTexture("guifermenter.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Fermenter", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        if (tile.isWorking()) {
            int m = (tile.getWorkMeter() * TANK_HEIGHT) / tile.getMaxWorkMeter();
            drawGradientRectFromDown(this, x + WORK_X, y + TANK_POS_Y, WORK_WIDTH, m, WORK_BOTTOM_COLOR, WORK_TOP_COLOR, TOP_DOWN, TANK_HEIGHT);
        }

        if (!tile.isEmpty()) {
            int m = (tile.getLiquidAmount() * TANK_HEIGHT) / TileFermenter.FERMENTER_CAPACITY;
            //DrawingHelper.drawRect(x + 24, y + 20, 16, m, liquidToColors.get(tile.getLiquid()));
            ColorPair pair = liquidToColors.get(tile.getLiquid());
            drawGradientRectFromDown(this, x + TANK_POS_X, y + TANK_POS_Y, TANK_WIDTH, m, pair.first, pair.second, LEFT_RIGHT, TANK_HEIGHT);
        }
    }
    // x, y, u, v, width, height


    @Override
    public List<String> fillTooltips(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        GuiFermenter fermenterGui = (GuiFermenter) gui;
        if (GuiHelper.isMouseInRect(fermenterGui, mousex, mousey, GuiFermenter.TANK_POS_X, GuiFermenter.TANK_POS_Y, GuiFermenter.TANK_WIDTH, GuiFermenter.TANK_HEIGHT)) {
            TileFermenter tile = fermenterGui.tile;
            currenttip.add(String.format("§2%s §8(§7%d§8/§7%d§8)§r", tile.getLiquid().getCapTitle(), tile.getLiquidAmount(), TileFermenter.FERMENTER_CAPACITY));
        } else if (GuiHelper.isMouseInRect(fermenterGui, mousex, mousey, GuiFermenter.WORK_X - 1, GuiFermenter.TANK_POS_Y, GuiFermenter.WORK_WIDTH + 1, GuiFermenter.TANK_HEIGHT)) {
            TileFermenter tile = fermenterGui.tile;
            String percent = tile.getMaxWorkMeter() == 0 ? "?" : String.format("%d", (tile.getWorkMeter() * 100) / tile.getMaxWorkMeter());
            currenttip.add(String.format("§7%s%%§r", percent));
        }
        return currenttip;
    }
}
