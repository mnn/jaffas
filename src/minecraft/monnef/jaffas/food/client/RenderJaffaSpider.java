/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.client.ResourcePathHelper;
import monnef.jaffas.food.entity.EntityJaffaSpider;
import monnef.jaffas.food.entity.EntityLittleSpider;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static monnef.core.client.ResourcePathHelper.ResourceTextureType.ENTITY;

@SideOnly(Side.CLIENT)
public class RenderJaffaSpider extends RenderLiving {

    private final ResourceLocation textureEyes;

    public RenderJaffaSpider(float shadowSize) {
        super(new ModelSpider(), shadowSize);
        this.setRenderPassModel(new ModelSpider());
        textureEyes = ResourcePathHelper.assembleAndCreate("jaffas_littleSpider_eyes.png",  ENTITY);
    }

    protected float setSpiderDeathMaxRotation() {
        return 180.0F;
    }

    protected int setSpiderEyeBrightness(EntityJaffaSpider spider, int par2) {
        if (par2 != 0) {
            return -1;
        } else {
            bindTexture(textureEyes);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (spider.isInvisible()) {
                GL11.glDepthMask(false);
            } else {
                GL11.glDepthMask(true);
            }

            char magic = 61680;
            int xCoord = magic % 65536;
            int yCoord = magic / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) xCoord / 1.0F, (float) yCoord / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            return 1;
        }
    }

    protected void scaleSpider(EntityJaffaSpider spider) {
        float scale = spider.spiderScaleAmount();
        GL11.glScalef(scale, scale, scale);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entity, float par2) {
        this.scaleSpider((EntityJaffaSpider) entity);
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase par1EntityLivingBase) {
        return this.setSpiderDeathMaxRotation();
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entity, int par2, float par3) {
        //return -1;
        return this.setSpiderEyeBrightness((EntityJaffaSpider) entity, par2);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return ((EntityLittleSpider) entity).getTexture();
    }
}
