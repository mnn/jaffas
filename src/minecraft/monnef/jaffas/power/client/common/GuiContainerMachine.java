package monnef.jaffas.power.client.common;

import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainerMachine extends GuiContainer {
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
        int texture = mc.renderEngine.getTexture(getBackgroundTexture());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    protected abstract String getBackgroundTexture();
}
