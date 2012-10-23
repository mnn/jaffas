package monnef.jaffas.food;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiFridge extends GuiContainer {


    private TileEntityFridge tileEntity;

    public GuiFridge(InventoryPlayer inventoryPlayer,
                     TileEntityFridge tileEntity) {
        //the container is instanciated and passed to the superclass for handling
        super(new ContainerFridge(inventoryPlayer, tileEntity));
        ySize = 198;

        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color
        fontRenderer.drawString("Fridge", 8, 3, 4210752);
        //draws "Inventory" or your regional equivalent
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        if (tileEntity.tickDivider == 1) {
            String s = String.valueOf(tileEntity.getTemperature());
            fontRenderer.drawString(s, 75, 3, 4210752);

            if (tileEntity.powerProvider != null) {
                s = String.valueOf(tileEntity.powerProvider.getEnergyStored());
                fontRenderer.drawString(s, 90, 15, 4210752);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        //draw your Gui here, only thing you need to change is the path
        int texture = mc.renderEngine.getTexture("/guifridge.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (tileEntity.isBurning()) {
            int burn = tileEntity.getBurnTimeRemainingScaled(14);
            this.drawTexturedModalRect(x + 103, y + 54 + (13 - burn), 176, 14 - burn, 14, burn);
        }

        int temp = Math.round(tileEntity.temperature);
        int tY = (int) Math.round(56 - temp * 1.2D);
        this.drawTexturedModalRect(x + 154, y + tY, 190, 0, 1, 105 - tY);
    }

}