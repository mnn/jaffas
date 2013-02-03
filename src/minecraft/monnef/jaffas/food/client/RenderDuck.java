package monnef.jaffas.food.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.entity.EntityDuck;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;

// mostly from chicken renderer
public class RenderDuck extends RenderLiving {
    @SideOnly(Side.CLIENT)
    public RenderDuck(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    public void renderDuck(EntityDuck duck, double par2, double par4, double par6, float par8, float par9) {
        super.doRenderLiving(duck, par2, par4, par6, par8, par9);
    }

    protected float getWingRotation(EntityDuck duck, float par2) {
        float var3 = duck.field_70888_h + (duck.field_70886_e - duck.field_70888_h) * par2;
        float var4 = duck.field_70884_g + (duck.destPos - duck.field_70884_g) * par2;
        return (MathHelper.sin(var3) + 1.0F) * var4;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2) {
        return this.getWingRotation((EntityDuck) par1EntityLiving, par2);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
        this.renderDuck((EntityDuck) par1EntityLiving, par2, par4, par6, par8, par9);
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderDuck((EntityDuck) par1Entity, par2, par4, par6, par8, par9);
    }
}
