package monnef.jaffas.food.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItemInAir extends Render {
    public RenderItemInAir(Item item) {
        super();
        textureFile = item.getIconFromDamage(0);
    }

    protected Icon textureFile;

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        // this.loadTexture(textureFile); //TODO?
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

        this.doRender(var10, this.textureFile);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void doRender(Tessellator par1Tessellator, Icon par2Icon) {
        float f = par2Icon.getMinU();
        float f1 = par2Icon.getMaxU();
        float f2 = par2Icon.getMinV();
        float f3 = par2Icon.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
        par1Tessellator.addVertexWithUV((double) (0.0F - f5), (double) (0.0F - f6), 0.0D, (double) f, (double) f3);
        par1Tessellator.addVertexWithUV((double) (f4 - f5), (double) (0.0F - f6), 0.0D, (double) f1, (double) f3);
        par1Tessellator.addVertexWithUV((double) (f4 - f5), (double) (f4 - f6), 0.0D, (double) f1, (double) f2);
        par1Tessellator.addVertexWithUV((double) (0.0F - f5), (double) (f4 - f6), 0.0D, (double) f, (double) f2);
        par1Tessellator.draw();
    }
}
