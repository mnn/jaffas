/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.entity;

import monnef.core.client.ResourcePathHelper;
import monnef.jaffas.food.client.Sounds;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IFactoryGrindable;
import powercrystals.minefactoryreloaded.api.MobDrop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static monnef.core.client.ResourcePathHelper.ResourceTextureType.ENTITY;
import static monnef.jaffas.food.JaffasFood.getItem;
import static monnef.jaffas.food.item.JaffaItem.duck;
import static monnef.jaffas.food.item.JaffaItem.duckEgg;
import static monnef.jaffas.food.item.JaffaItem.duckRaw;
import static monnef.jaffas.food.item.JaffaItem.featherDuck;

public class EntityDuck extends EntityAnimal {
    public float pos = 0.0F;
    public float destPos = 0.0F;
    public float destPosOld;
    public float posOld;
    public float field_70889_i = 1.0F;

    public static final Item feather = getItem(featherDuck);
    public static final Item egg = getItem(duckEgg);
    public static final Item rawMeat = getItem(duckRaw);
    public static final Item cookedMeat = getItem(duck);

    /**
     * The time until the next egg is spawned.
     */
    public int timeUntilNextEgg;

    public EntityDuck(World par1World) {
        super(par1World);
        this.setSize(0.3F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.wheat_seeds, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.posOld = this.pos;
        this.destPosOld = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);

        if (this.destPos < 0.0F) {
            this.destPos = 0.0F;
        }

        if (this.destPos > 1.0F) {
            this.destPos = 1.0F;
        }

        if (!this.onGround && this.field_70889_i < 1.0F) {
            this.field_70889_i = 1.0F;
        }

        this.field_70889_i = (float) ((double) this.field_70889_i * 0.9D);

        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        this.pos += this.field_70889_i * 2.0F;

        if (!this.isChild() && !this.worldObj.isRemote && --this.timeUntilNextEgg <= 0) {
            boolean isFeather = rand.nextInt(5) == 0;

            if (!isFeather)
                this.playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

            this.dropItem(isFeather ? feather : egg, 1);

            this.timeUntilNextEgg = this.rand.nextInt(8000) + 4000;
        }
    }

    @Override
    protected void fall(float par1) {
    }

    @Override
    protected String getLivingSound() {
        return Sounds.SoundsEnum.DUCK.getSoundName();
    }

    @Override
    protected float getSoundVolume() {
        return 0.2f;
    }

    @Override
    protected String getHurtSound() {
        return "mob.chicken.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.chicken.hurt";
    }

    @Override
    protected void func_145780_a(int par1, int par2, int par3, Block p_145780_4_) {
        this.playSound("mob.chicken.step", 0.15F, 1.0F);
    }

    @Override
    protected Item getDropItem() {
        return feather;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    @Override
    protected void dropFewItems(boolean par1, int fortune) {
        int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + fortune);

        for (int var4 = 0; var4 < var3; ++var4) {
            this.dropItem(feather, 1);
        }

        if (this.isBurning()) {
            this.dropItem(cookedMeat, 1);
        } else {
            this.dropItem(rawMeat, 1);
        }
    }

    public EntityDuck spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
        return new EntityDuck(this.worldObj);
    }

    @Override
    public boolean isBreedingItem(ItemStack par1ItemStack) {
        return par1ItemStack != null && par1ItemStack.getItem() instanceof ItemSeeds;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
        return this.spawnBabyAnimal(par1EntityAgeable);
    }

    public static class MFR implements IFactoryGrindable {
        @Override
        public Class<? extends EntityLivingBase> getGrindableEntity() {
            return EntityDuck.class;
        }

        @Override
        public List<MobDrop> grind(World world, EntityLivingBase entity, Random random) {
            ArrayList<MobDrop> res = new ArrayList<MobDrop>();
            res.add(new MobDrop(3, new ItemStack(rawMeat, 1, 0)));
            res.add(new MobDrop(1, new ItemStack(feather, 1, 0)));
            return res;
        }

        @Override
        public boolean processEntity(EntityLivingBase entity) {
            return true;
        }
    }


    public static String getTexturePath() {
        return ResourcePathHelper.assemble("jaffas_duck.png", ENTITY);
    }
}
