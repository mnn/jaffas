/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.core.client.DrawingHelper;
import monnef.jaffas.technic.block.ContainerFermenter;
import monnef.jaffas.technic.block.TileEntityFermenter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

import static monnef.core.utils.ColorHelper.IntColor;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.BEER;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.BEER_RAW;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.WINE;
import static monnef.jaffas.technic.block.TileEntityFermenter.FermentedLiquid.WINE_RAW;

public class GuiFermenter extends GuiContainer {
    public static final String GUI_TEXTURE = "/guibreaker.png";

    public TileEntityFermenter tile;

    private static HashMap<FermentedLiquid, IntColor> liquidToColor;

    static {
        liquidToColor = new HashMap<FermentedLiquid, IntColor>();

        liquidToColor.put(BEER, new IntColor(220, 220, 90));
        liquidToColor.put(BEER_RAW, new IntColor(225, 175, 70));

        liquidToColor.put(WINE_RAW, new IntColor(200, 70, 125));
        liquidToColor.put(WINE, new IntColor(190, 35, 90));
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

        // arrow
        if (tile.isWorking()) {
            int m = (tile.getWorkMeter() * 24) / tile.getMaxWorkMeter();
            drawTexturedModalRect(x + 84, y + 34, 176, 14, m + 1, 16);
        }

        if (!tile.isEmpty()) {
            int m = (tile.getLiquidAmount() * 46) / TileEntityFermenter.FERMENTER_CAPACITY;
            DrawingHelper.drawRect(x + 76, y + 20, 16, m, liquidToColor.get(tile.getLiquid()));
        }
    }
    // x, y, u, v, width, height
}
