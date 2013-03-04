package monnef.jaffas.trees.client;

import monnef.jaffas.trees.block.ContainerFruitCollector;
import monnef.jaffas.trees.TileEntityFruitCollector;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiFruitCollector extends GuiContainer {


    private TileEntityFruitCollector tileEntity;

    public GuiFruitCollector(InventoryPlayer inventoryPlayer,
                             TileEntityFruitCollector tileEntity) {
        //the container is instanciated and passed to the superclass for handling
        super(new ContainerFruitCollector(inventoryPlayer, tileEntity));
        //ySize = 198;

        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color
        this.fontRenderer.drawString("Fruit Collector", 8, 5, 4210752);

        //draws "Inventory" or your regional equivalent
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        //draw your Gui here, only thing you need to change is the path
        int texture = mc.renderEngine.getTexture("/guicollector.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (tileEntity.isBurning()) {
            int burn = tileEntity.getBurnTimeRemainingScaled(14);
            this.drawTexturedModalRect(x + 105, y + 24 + (13 - burn), 176, 14 - burn, 14, burn);
        }

        /*
        int temp = Math.round(tileEntity.temperature);
        int tY = (int) Math.round(56 - temp * 1.2D);
        this.drawTexturedModalRect(x + 154, y + tY, 190, 0, 1, 105 - tY);
        */
    }

}