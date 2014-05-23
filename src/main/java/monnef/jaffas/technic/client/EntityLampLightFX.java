/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityLampLightFX extends EntityFX {
    public EntityLampLightFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, double lifeConstant) {
        super(world, x, y, z, motionX, motionY, motionZ);
        float f = this.rand.nextFloat() * 0.3F + 0.7F;
        this.particleRed = f;
        this.particleGreen = f;
        this.particleBlue = f - 0.5f;
        this.particleAlpha = 0.33f;
        this.setParticleTextureIndex(0);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.4F + 0.6F;
        this.motionX = motionX * 0.05D;
        this.motionY = motionY * 0.05D;
        this.motionZ = motionZ * 0.05D;

        this.particleMaxAge = (int) (lifeConstant / (Math.random() * 0.2D + 0.8D));
        this.noClip = true;
    }

    public void configureColor(float red, float green, float blue) {
        particleRed = red;
        particleGreen = green;
        particleBlue = blue;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        /*
        this.motionX *= 0.99D;
        this.motionY *= 0.99D;
        this.motionZ *= 0.99D;
        */

        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }
}
