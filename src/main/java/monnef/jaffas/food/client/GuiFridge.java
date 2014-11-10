/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.client.GuiContainerMonnefCore;
import monnef.core.utils.LanguageHelper;
import monnef.jaffas.food.block.ContainerFridge;
import monnef.jaffas.food.block.TileFridge;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiFridge extends GuiContainerMonnefCore {
    private TileFridge tileEntity;

    public GuiFridge(InventoryPlayer inventoryPlayer, TileFridge tileEntity, ContainerFridge container) {
        super(container);

        this.tileEntity = tileEntity;
        setBackgroundTexture("guifridge.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString("Fridge", 8, 3, 4210752);
        fontRendererObj.drawString(LanguageHelper.localInventory(), 8, ySize - 96 + 2, 4210752);

        if (TileFridge.tickDivider == 1) {
            String s = String.valueOf(tileEntity.getTemperature());
            fontRendererObj.drawString(s, 75, 3, 4210752);

            s = String.valueOf(tileEntity.getEnergyStorage().getEnergyStored());
            fontRendererObj.drawString(s, 90, 15, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        int temp = Math.round(tileEntity.temperature);
        int tY = (int) Math.round(56 - temp * 1.2D);
        this.drawTexturedModalRect(x + 154, y + tY, 190, 0, 1, 105 - tY);
    }

}