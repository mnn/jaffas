/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.core.utils.ColorEnum;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.MathHelper;
import monnef.jaffas.power.block.TileWindGenerator;
import monnef.jaffas.power.block.WindGeneratorFreeSpaceHelper;
import monnef.core.block.ContainerMachine;
import monnef.core.client.GuiContainerMachine;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.List;

public class GuiContainerWindGenerator extends GuiContainerMachine {
    private static final int CHECK_SPACE_BUTTON_ID = 52;

    private final TileWindGenerator generator;

    private String checkText = constructFreeText("?");
    private static final int CHECK_SPACE_Y_OFFSET = 52;
    private GuiButton safeRadButton;

    public GuiContainerWindGenerator(InventoryPlayer inventoryPlayer, TileWindGenerator tileEntity, ContainerMachine container) {
        super(inventoryPlayer, tileEntity, container);
        this.generator = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();
        safeRadButton = new GuiButton(CHECK_SPACE_BUTTON_ID, x + 8, y + CHECK_SPACE_Y_OFFSET - 3, 100, 20, checkText);
        buttonList.add(safeRadButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int value = MathHelper.scaleValue(generator.getTurbineSpeed(), TileWindGenerator.TURBINE_MAX_SPEED, ENERGY_BAR_HEIGHT - 2);
        drawBottomUpBar(x + ENERGY_BAR_X, y + ENERGY_BAR_Y, value, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, ColorHelper.getColor(ColorEnum.LIGHT_BLUE), ColorHelper.getColor(ColorEnum.BLUE));
    }

    @Override
    public List<String> fillTooltips(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        if (isMouseInRect(mousex, mousey, ENERGY_BAR_X, ENERGY_BAR_Y, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT)) {
            currenttip.add("\u00A72Speed:\u00A7r");
            currenttip.add(String.format(" \u00A77%d\u00A78/\u00A77%d\u00A7r", generator.getTurbineSpeed(), TileWindGenerator.TURBINE_MAX_SPEED));
            currenttip.add("\u00A72Production:\u00A7r");
            currenttip.add(String.format(" \u00A77%.1f \u00A78MJ/t\u00A7r", generator.getLastPowerProduction() / 10f));
        }

        return currenttip;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case CHECK_SPACE_BUTTON_ID:
                WindGeneratorFreeSpaceHelper.FreeSpaceResult res = WindGeneratorFreeSpaceHelper.compute(generator);
                checkText = constructFreeText(res.getFreeRadius() >= 0 ? res.getFreeRadius() + ".5" : "0");
                refreshButtonText();
                break;
        }
    }

    private void refreshButtonText() {
        safeRadButton.displayString = checkText;
    }

    private String constructFreeText(String radius) {
        return "safe radius: " + radius;
    }
}
