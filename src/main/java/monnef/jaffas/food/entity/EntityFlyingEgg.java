package monnef.jaffas.food.entity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class EntityFlyingEgg extends EntityThrowable {
    public EntityFlyingEgg(World par1World) {
        super(par1World);
    }

    public EntityFlyingEgg(World par1World, EntityLivingBase par2EntityLiving) {
        super(par1World, par2EntityLiving);
    }

    public EntityFlyingEgg(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
    }

    // based on EntityEgg
    @Override
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
        if (par1MovingObjectPosition.entityHit != null) {
            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
        }

        if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0) {
            byte countOfEntitiesToSpawn = 1;

            if (this.rand.nextInt(4) == 0) {
                countOfEntitiesToSpawn = 2;
            }

            for (int i = 0; i < countOfEntitiesToSpawn; ++i) {
                Entity entityToSpawn = createEntity();
                entityToSpawn.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(entityToSpawn);
            }
        }

        if (worldObj.isRemote) {
            for (int i = 0; i < 8; ++i) {
                //this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                spawnBreakingParticles(worldObj, this.posX, this.posY, this.posZ, itemForParticleEffect());
            }
        }

        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnBreakingParticles(World world, double x, double y, double z, Item item) {
        EntityBreakingFX e = new EntityBreakingFX(world, x, y, z, item);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(e);
    }

    protected abstract Entity createEntity();

    protected abstract Item itemForParticleEffect();
}
