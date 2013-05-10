/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityDuckEgg extends EntityThrowable {
    public EntityDuckEgg(World par1World) {
        super(par1World);
    }

    public EntityDuckEgg(World par1World, EntityLiving par2EntityLiving) {
        super(par1World, par2EntityLiving);
    }

    public EntityDuckEgg(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
    }

    // based on EntityEgg
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
        if (par1MovingObjectPosition.entityHit != null) {
            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
        }

        if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0) {
            byte ducksToSpawn = 1;

            if (this.rand.nextInt(32) == 0) {
                ducksToSpawn = 4;
            }

            for (int i = 0; i < ducksToSpawn; ++i) {
                EntityDuck duck = new EntityDuck(this.worldObj);
                duck.setGrowingAge(-24000);
                duck.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(duck);
            }
        }

        for (int i = 0; i < 8; ++i) {
            this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }
}
