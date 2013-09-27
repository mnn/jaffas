package monnef.jaffas.food.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.RandomHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IFactoryGrindable;
import powercrystals.minefactoryreloaded.api.MobDrop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static monnef.jaffas.food.JaffasFood.getItem;
import static monnef.jaffas.food.item.JaffaItem.spiderLegRaw;

public class EntityLittleSpider extends EntityJaffaSpider {
    public static final int spiderMeat = getItem(spiderLegRaw).itemID;
    private static final String AGGRESSIVE_TIMER_TAG = "aggressiveTimer";
    private static final String WEB_TIMER_TAG = "webTimer";
    private static final String COLOR_TAG = "spiderColor";
    private static final int COLOR_WATCHER_INDEX = 25;
    public static final int COLORS_COUNT = 3;
    public static final String TEXTURE_STRING = "/jaffas_littleSpider_%d.png";
    public static final ResourceLocation[] TEXTURES;
    public static final float LIVING_SOUND_VOLUME = 0.2f;
    private int timeUntilNextWeb;
    private int aggressiveTime;

    static {
        TEXTURES = new ResourceLocation[COLORS_COUNT];
        for (int i = 0; i < COLORS_COUNT; i++) {
            TEXTURES[i] = new ResourceLocation(generateTextureFileName(i));
        }
    }

    private static String generateTextureFileName(int color) {
        return String.format(TEXTURE_STRING, color);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURES[getColor()];
    }


    public EntityLittleSpider(World world) {
        super(world);
        //this.setSize(1.4F, 0.9F);
        this.setSize(0.5F, 0.25F);
        this.timeUntilNextWeb = this.rand.nextInt(6000) + 6000;
        this.renderDistanceWeight = 2;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(12D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.8D);
    }

    @Override
    protected Entity findPlayerToAttack() {
        return isAggressive() ? this.worldObj.getClosestVulnerablePlayerToEntity(this, 18) : null;
    }

    @Override
    public boolean isOnLadder() {
        return isAggressive() ? this.isBesideClimbableBlock() : false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float spiderScaleAmount() {
        return 0.4F;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(COLOR_WATCHER_INDEX, (byte) rand.nextInt(COLORS_COUNT));
    }

    protected void setColor(byte color) {
        this.dataWatcher.updateObject(COLOR_WATCHER_INDEX, color);
    }

    public byte getColor() {
        return this.dataWatcher.getWatchableObjectByte(COLOR_WATCHER_INDEX);
    }

    @Override
    protected void dropFewItems(boolean attackedByPlayer, int lootingLevel) {
        super.dropFewItems(attackedByPlayer, lootingLevel);
    }

    @Override
    protected void processAdditionalDrops(boolean killedByPlayer, int lootingLevel) {
        if (killedByPlayer && (this.rand.nextInt(2) == 0 || this.rand.nextInt(1 + lootingLevel) > 1)) {
            this.dropItem(Item.silk.itemID, 1);
        }
    }

    public static class MFR implements IFactoryGrindable {
        @Override
        public Class<?> getGrindableEntity() {
            return EntityLittleSpider.class;
        }

        @Override
        public List<MobDrop> grind(World world, EntityLiving entity, Random random) {
            ArrayList<MobDrop> res = new ArrayList<MobDrop>();
            res.add(new MobDrop(3, new ItemStack(spiderMeat, 1, 0)));
            res.add(new MobDrop(2, new ItemStack(Item.silk, 1, 0)));
            return res;
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (isAggressive()) {
            aggressiveTime -= 1;
        }

        if (!this.isChild() && !this.worldObj.isRemote && !isAggressive() && --this.timeUntilNextWeb <= 0) {
            int x = (int) Math.round(posX);
            int y = (int) Math.round(posY);
            int z = (int) Math.round(posZ);
            if (!worldObj.isAirBlock(x, y, z) && worldObj.getBlockId(x, y, z) == Block.web.blockID) {
                y++;
                if (!worldObj.isAirBlock(x, y, z) && worldObj.getBlockId(x, y, z) == Block.web.blockID && rand.nextInt(2) == 0) {
                    y++;
                }
            }
            if (worldObj.isAirBlock(x, y, z)) {
                this.timeUntilNextWeb = this.rand.nextInt(8000) + 4000;
                BlockHelper.setBlock(worldObj, x, y, z, Block.web.blockID);
                playWebSound();
            } else {
                // failed lookup for free space, trying again after some time
                this.timeUntilNextWeb = 20 * 30;
            }
        }
    }

    private boolean isAggressive() {
        return aggressiveTime > 0;
    }

    @Override
    public boolean canSpawnWithSkeleton() {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damageAmount) {
        boolean ret = super.attackEntityFrom(source, damageAmount);
        aggressiveTime = ret ? RandomHelper.generateRandomFromInterval(30 * 20, 120 * 20) : 0;
        return ret;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger(AGGRESSIVE_TIMER_TAG, aggressiveTime);
        tag.setInteger(WEB_TIMER_TAG, timeUntilNextWeb);
        tag.setByte(COLOR_TAG, getColor());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        aggressiveTime = tag.getInteger(AGGRESSIVE_TIMER_TAG);
        timeUntilNextWeb = tag.getInteger(WEB_TIMER_TAG);
        setColor(tag.getByte(COLOR_TAG));
    }

    @Override
    protected float getSoundVolume() {
        return 0.8f;
    }

    @Override
    public void playLivingSound() {
        this.playSound(this.getLivingSound(), LIVING_SOUND_VOLUME, this.getSoundPitch());
    }

    public void playWebSound() {
        this.playSound(this.getLivingSound(), this.getSoundVolume(), this.getSoundPitch());
    }
}
