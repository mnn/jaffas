/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.entity.EntityJaffaSpider;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderJaffaSpider extends RenderLiving {
    public RenderJaffaSpider(float shadowSize) {
        super(new ModelSpider(), shadowSize);
        this.setRenderPassModel(new ModelSpider());
    }

    protected float setSpiderDeathMaxRotation() {
        return 180.0F;
    }

    protected int setSpiderEyeBrightness(EntityJaffaSpider spider, int par2) {
        if (par2 != 0) {
            return -1;
        } else {
            this.loadTexture("/jaffas_littleSpider_eyes.png");
            float f1 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (spider.isInvisible()) {
                GL11.glDepthMask(false);
            } else {
                GL11.glDepthMask(true);
            }

            char c0 = 61680;
            int j = c0 % 65536;
            int k = c0 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            return 1;
        }
    }

    protected void scaleSpider(EntityJaffaSpider spider) {
        float scale = spider.spiderScaleAmount();
        GL11.glScalef(scale, scale, scale);
    }

    @Override
    protected void preRenderCallback(EntityLiving entity, float par2) {
        this.scaleSpider((EntityJaffaSpider) entity);
    }

    @Override
    protected float getDeathMaxRotation(EntityLiving entity) {
        return this.setSpiderDeathMaxRotation();
    }

    @Override
    protected int shouldRenderPass(EntityLiving entity, int par2, float par3) {
        //return -1;
        return this.setSpiderEyeBrightness((EntityJaffaSpider) entity, par2);
    }
}
