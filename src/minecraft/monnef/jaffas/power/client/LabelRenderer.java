/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class LabelRenderer {

    private RenderManager renderManager;

    public LabelRenderer() {
        renderManager = RenderManager.instance;
    }

    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }

    protected void renderLabel(TileEntity tileEntity, String text, int visibleDistance, double x, double y, double z, int textWidth, int lines) {
        EntityLivingBase player = this.renderManager.livingPlayer;
        double distanceToEntity = tileEntity.getDistanceFrom(player.posX, player.posY, player.posZ);

        if (distanceToEntity <= visibleDistance) {
            FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
            float var13 = 1f; // 1.6F;
            float size = 0.016666668F * var13;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.0F, (float) z + 0.5F);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-size, -size, size);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var15 = Tessellator.instance;
            byte topCoord = 0;
            int mysteryNumber = 120;

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var15.startDrawingQuads();
            int var17 = textWidth;
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.33F); //orig: 0.25

            double x1 = (double) (-var17 - 1);
            double x2 = (double) (var17 + 1);
            double y1 = (double) (-1 + topCoord);
            double y2 = (double) (2 + 8 * lines + topCoord);
            var15.addVertex(x1, y1, 0.0D);
            var15.addVertex(x1, y2, 0.0D);
            var15.addVertex(x2, y2, 0.0D);
            var15.addVertex(x2, y1, 0.0D);
            var15.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            int leftCoord = -textWidth;
            int grayColor = 553648127;
            fontRenderer.drawSplitString(text, leftCoord, topCoord, mysteryNumber, -1);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontRenderer.drawSplitString(text, leftCoord, topCoord, mysteryNumber, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
