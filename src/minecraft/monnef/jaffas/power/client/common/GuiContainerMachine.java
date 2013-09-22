/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client.common;

import monnef.core.client.GuiContainerJaffas;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiContainerMachine extends GuiContainerJaffas {
    public static final int ENERGY_BAR_X = 146;
    public static final int ENERGY_BAR_Y = 15;
    public static final int ENERGY_BAR_HEIGHT = 50;
    public static final int ENERGY_BAR_INNER_HEIGHT = ENERGY_BAR_HEIGHT - 2;
    public static final int ENERGY_BAR_WIDTH = 18;
    protected static ColorHelper.IntColor topColor = new ColorHelper.IntColor(53, 180, 212);
    protected static ColorHelper.IntColor bottomColor = new ColorHelper.IntColor(25, 78, 90);
    protected final TileEntityMachineWithInventory tile;
    private String backgroundTexture = "/guimachine.png";

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

    protected String getTitle() {
        return tile.getMachineTitle();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(getBackgroundTexture());
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (tile.isPowerBarRenderingEnabled())
            drawEnergyBar(tile);
    }

    protected String getBackgroundTexture() {
        return backgroundTexture;
    }

    public void setBackgroundTexture(String backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    protected void drawEnergyBar(TileEntityMachineWithInventory tile) {
        int value = tile.powerMax != 0 ? (tile.powerStored * ENERGY_BAR_INNER_HEIGHT) / tile.powerMax : 0;
        drawBottomUpBar(x + ENERGY_BAR_X, y + ENERGY_BAR_Y, value, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, topColor, bottomColor);
    }

    @Override
    public List<String> fillTooltips(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        if (GuiHelper.isMouseInRect(this, mousex, mousey, ENERGY_BAR_X, ENERGY_BAR_Y, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT)) {
            currenttip.add("§2Energy:§r");
            currenttip.add(String.format("§7%d§8 / §7%d§r", tile.powerStored, tile.powerMax));
        }

        return currenttip;
    }
}
