/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

// based on EntitySpider
public class EntityJaffaSpider extends EntityCreatureMob {

    public static final int CLIMBABLE_BLOCK_WATCHER_INDEX = 16;
    public static final ResourceLocation TEXTURE = new ResourceLocation("/mob/spider.png");

    public EntityJaffaSpider(World world) {
        super(world);
        this.setSize(1.4F, 0.9F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.8D);
    }

    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(CLIMBABLE_BLOCK_WATCHER_INDEX, (byte) 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.worldObj.isRemote) {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.height * 0.75D - 0.5D;
    }

    @Override
    protected Entity findPlayerToAttack() {
        float f = this.getBrightness(1.0F);

        if (f < 0.5F) {
            double d0 = 16.0D;
            return this.worldObj.getClosestVulnerablePlayerToEntity(this, d0);
        } else {
            return null;
        }
    }

    @Override
    protected String getLivingSound() {
        return "mob.spider.say";
    }

    @Override
    protected String getHurtSound() {
        return "mob.spider.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.spider.death";
    }

    @Override
    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
        this.playSound("mob.spider.step", 0.15F, 1.0F);
    }

    @Override
    protected void attackEntity(Entity par1Entity, float par2) {
        float f1 = this.getBrightness(1.0F);

        if (f1 > 0.5F && this.rand.nextInt(100) == 0) {
            this.entityToAttack = null;
        } else {
            if (par2 > 2.0F && par2 < 6.0F && this.rand.nextInt(10) == 0) {
                if (this.onGround) {
                    double d0 = par1Entity.posX - this.posX;
                    double d1 = par1Entity.posZ - this.posZ;
                    float f2 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
                    this.motionX = d0 / (double) f2 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
                    this.motionZ = d1 / (double) f2 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
                    this.motionY = 0.4000000059604645D;
                }
            } else {
                super.attackEntity(par1Entity, par2);
            }
        }
    }

    @Override
    protected Item getDropItem() {
        return Items.string;
    }

    @Override
    protected void dropFewItems(boolean killedByPlayer, int lootingLevel) {
        super.dropFewItems(killedByPlayer, lootingLevel);
        processAdditionalDrops(killedByPlayer, lootingLevel);
    }

    protected void processAdditionalDrops(boolean killedByPlayer, int lootingLevel) {
        dropSpiderEye(killedByPlayer, lootingLevel);
    }

    protected void dropSpiderEye(boolean killedByPlayer, int lootingLevel) {
        if (killedByPlayer && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + lootingLevel) > 0)) {
            this.dropItem(Items.spider_eye, 1);
        }
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    @Override
    public void setInWeb() {
        // immune to webs
    }

    @SideOnly(Side.CLIENT)
    public float spiderScaleAmount() {
        return 1.0F;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
        return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
    }

    public boolean isBesideClimbableBlock() {
        return (this.dataWatcher.getWatchableObjectByte(CLIMBABLE_BLOCK_WATCHER_INDEX) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean value) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(CLIMBABLE_BLOCK_WATCHER_INDEX);

        if (value) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 &= -2;
        }

        this.dataWatcher.updateObject(CLIMBABLE_BLOCK_WATCHER_INDEX, Byte.valueOf(b0));
    }

    public boolean canSpawnWithSkeleton() {
        return true;
    }
}
