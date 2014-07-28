/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.entity.EntityDuck;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

// mostly from chicken renderer
public class RenderDuck extends RenderLiving {
    private static final ResourceLocation texture = new ResourceLocation(EntityDuck.getTexturePath());

    @SideOnly(Side.CLIENT)
    public RenderDuck(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    public void renderDuck(EntityDuck duck, double par2, double par4, double par6, float par8, float par9) {
        super.func_110827_b(duck, par2, par4, par6, par8, par9); // doRenderLiving
    }

    protected float getWingRotation(EntityDuck duck, float par2) {
        float var3 = duck.posOld + (duck.pos - duck.posOld) * par2;
        float var4 = duck.destPosOld + (duck.destPos - duck.destPosOld) * par2;
        return (MathHelper.sin(var3) + 1.0F) * var4;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(EntityLivingBase par1EntityLiving, float par2) {
        return this.getWingRotation((EntityDuck) par1EntityLiving, par2);
    }

    @Override
    public void func_110827_b(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
        this.renderDuck((EntityDuck) par1EntityLiving, par2, par4, par6, par8, par9);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderDuck((EntityDuck) par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
