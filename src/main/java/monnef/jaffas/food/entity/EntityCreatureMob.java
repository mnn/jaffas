/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

// heavily based on EntityMob
public abstract class EntityCreatureMob extends EntityCreature {
    public EntityCreatureMob(World world) {
        super(world);
        this.experienceValue = 5;
    }

    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        float light = this.getBrightness(1.0F);

        if (light > 0.5F) {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (shouldDespawnInPeaceful() && !this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected Entity findPlayerToAttack() {
        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damageAmount) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else if (super.attackEntityFrom(source, damageAmount)) {
            Entity entity = source.getEntity();

            if (this.riddenByEntity != entity && this.ridingEntity != entity) {
                if (entity != this) {
                    this.entityToAttack = entity;
                }

                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        int dmg = this.getAttackStrength(entity);

        if (this.isPotionActive(Potion.damageBoost)) {
            dmg += 3 << this.getActivePotionEffect(Potion.damageBoost).getAmplifier();
        }

        if (this.isPotionActive(Potion.weakness)) {
            dmg -= 2 << this.getActivePotionEffect(Potion.weakness).getAmplifier();
        }

        int knockBack = 0;

        if (entity instanceof EntityLiving) {
            dmg += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLiving) entity);
            knockBack += EnchantmentHelper.getKnockbackModifier(this, (EntityLiving) entity);
        }

        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), dmg);

        if (flag) {
            if (knockBack > 0) {
                entity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) knockBack * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) knockBack * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int k = EnchantmentHelper.getFireAspectModifier(this);

            if (k > 0) {
                entity.setFire(k * 4);
            }

            if (entity instanceof EntityLivingBase) {
                //EnchantmentThorns.func_92096_a(this, (EntityLiving) entity, this.rand);
                EnchantmentHelper.func_151384_a((EntityLivingBase) entity, this);
            }

            EnchantmentHelper.func_151385_b(this, entity);
        }

        return flag;
    }

    @Override
    protected void attackEntity(Entity entity, float distance) {
        if (this.attackTime <= 0 && distance < 2.0F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            this.attackEntityAsMob(entity);
        }
    }

    @Override
    public float getBlockPathWeight(int x, int y, int z) {
        return 0.5F - this.worldObj.getLightBrightness(x, y, z);
    }

    protected boolean isValidLightLevel() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.boundingBox.minY);
        int z = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) > this.rand.nextInt(32)) {
            return false;
        } else {
            int lightValue = this.worldObj.getBlockLightValue(x, y, z);

            if (this.worldObj.isThundering()) {
                int tmp = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                lightValue = this.worldObj.getBlockLightValue(x, y, z);
                this.worldObj.skylightSubtracted = tmp;
            }

            return lightValue <= this.rand.nextInt(8);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.isValidLightLevel() && super.getCanSpawnHere();
    }

    public int getAttackStrength(Entity entity) {
        return 2;
    }
}
