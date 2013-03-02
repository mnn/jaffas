package monnef.jaffas.food.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItemInAir extends Render {
    public RenderItemInAir(Item item) {
        super();
        itemIconIndex = item.getIconFromDamage(0);
        textureFile = item.getTextureFile();
    }

    protected int itemIconIndex;
    protected String textureFile;

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.loadTexture(textureFile);
        Tessellator var10 = Tessellator.instance;

        /*
        if (this.itemIconIndex == 154) {
            int var11 = PotionHelper.func_77915_a(((EntityPotion) par1Entity).getPotionDamage(), false);
            float var12 = (float) (var11 >> 16 & 255) / 255.0F;
            float var13 = (float) (var11 >> 8 & 255) / 255.0F;
            float var14 = (float) (var11 & 255) / 255.0F;
            GL11.glColor3f(var12, var13, var14);
            GL11.glPushMatrix();
            this.func_77026_a(var10, 141);
            GL11.glPopMatrix();
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
        }
        */

        this.doRender(var10, this.itemIconIndex);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void doRender(Tessellator tessellator, int iconIndex) {
        float x1 = (float) (iconIndex % 16 * 16 + 0) / 256.0F;
        float x2 = (float) (iconIndex % 16 * 16 + 16) / 256.0F;
        float y2 = (float) (iconIndex / 16 * 16 + 0) / 256.0F;
        float y1 = (float) (iconIndex / 16 * 16 + 16) / 256.0F;
        float var7 = 1.0F;
        float var8 = 0.5F;
        float var9 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double) (0.0F - var8), (double) (0.0F - var9), 0.0D, (double) x1, (double) y1);
        tessellator.addVertexWithUV((double) (var7 - var8), (double) (0.0F - var9), 0.0D, (double) x2, (double) y1);
        tessellator.addVertexWithUV((double) (var7 - var8), (double) (var7 - var9), 0.0D, (double) x2, (double) y2);
        tessellator.addVertexWithUV((double) (0.0F - var8), (double) (var7 - var9), 0.0D, (double) x1, (double) y2);
        tessellator.draw();
    }
}
