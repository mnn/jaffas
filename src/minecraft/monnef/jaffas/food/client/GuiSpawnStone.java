/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.utils.MathHelper;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.common.CoolDownType;
import monnef.jaffas.food.common.SpawnStonePacketUtils;
import monnef.jaffas.food.item.ItemSpawnStone;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSpawnStone extends GuiScreen {
    public static final int xSizeOfTexture = 176;
    public static final int ySizeOfTexture = 88;
    private EntityPlayer player;
    private ItemSpawnStone stone;
    private String coolDownText;
    private GuiButton buttonUse;
    private GuiButton buttonClose;
    private RenderItem itemRenderer = new RenderItem();
    private final ItemStack stack;

    public GuiSpawnStone(EntityPlayer player, ItemSpawnStone stone) {
        super();
        this.player = player;
        this.stone = stone;
        coolDownText = Integer.toString(stone.getCoolDownInMinutes());
        stack = new ItemStack(stone);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        drawDefaultBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("/guispawnstone.png"));

        int posX = (this.width - xSizeOfTexture) / 2;
        int posY = (this.height - ySizeOfTexture) / 2;

        drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);

        StringBuilder text = new StringBuilder("   Home Stone\n\nStone's cool-down: ");
        text.append(coolDownText);
        text.append("m\nYour active cool-down: ");
        text.append(MathHelper.oneDecimalPlace.format(CoolDownRegistry.getRemainingCoolDownInSeconds(player.getEntityName(), CoolDownType.SPAWN_STONE) / 60f));
        text.append("m");

        //fontRenderer.drawString(text.toString(), posX + 10, posY + 20, 4210752);
        fontRenderer.drawSplitString(text.toString(), posX + 10, posY + 15, 150, 4210752);

        buttonUse.enabled = !CoolDownRegistry.isCoolDownActive(player.getEntityName(), CoolDownType.SPAWN_STONE);

        itemRenderer.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, stack, posX + 90, posY + 10);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);

        super.drawScreen(x, y, f);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        int posX = (this.width - xSizeOfTexture) / 2;
        int posY = (this.height - ySizeOfTexture) / 2;

        buttonUse = new GuiButton(0, posX + 90, posY + 60, 70, 20, "Use");
        this.buttonList.add(buttonUse);

        buttonClose = new GuiButton(1, posX + 15, posY + 60, 70, 20, "Close");
        this.buttonList.add(buttonClose);
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

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        switch (par1GuiButton.id) {
            case 0:
                SpawnStonePacketUtils.sendPortPacket();
                this.mc.thePlayer.closeScreen();
                break;

            case 1:
                this.mc.thePlayer.closeScreen();
                break;
        }
    }
}
