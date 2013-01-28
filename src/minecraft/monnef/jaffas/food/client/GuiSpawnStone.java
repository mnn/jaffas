package monnef.jaffas.food.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiSpawnStone extends GuiScreen {
    public static final int xSizeOfTexture = 176;
    public static final int ySizeOfTexture = 88;
    private int cooldown;
    private String cooldownText;

    public GuiSpawnStone(EntityPlayer player, int cooldown) {
        super();
        this.cooldown = cooldown;
        cooldownText = Integer.toString(cooldown);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        drawDefaultBackground();

        int var4 = this.mc.renderEngine.getTexture("/guispawnstone.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);

        int posX = (this.width - xSizeOfTexture) / 2;
        int posY = (this.height - ySizeOfTexture) / 2;

        drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);

        fontRenderer.drawString(cooldownText, posX + 10, posY + 20, -1);

        super.drawScreen(x, y, f);
    }

    @Override
    public void initGui() {
        this.controlList.clear();
        int posX = (this.width - xSizeOfTexture) / 2;
        int posY = (this.height - ySizeOfTexture) / 2;

        this.controlList.add(new GuiButton(0, posX + 40, posY + 40, 100, 20, "Use"));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.thePlayer.closeScreen();
        }
    }
}
