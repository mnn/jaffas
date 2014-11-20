package monnef.jaffas.food.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.MonnefCorePlugin;
import monnef.core.client.ResourcePathHelper;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.RandomHelper;
import monnef.jaffas.food.common.CobWebDescriptor;
import monnef.jaffas.food.common.CobWebInfluencingBlocksCalculator;
import monnef.jaffas.food.common.CobWebInfluencingBlocksCalculator$;
import monnef.jaffas.food.common.SpecialCobWebRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IFactoryGrindable;
import powercrystals.minefactoryreloaded.api.MobDrop;
import scala.Tuple2;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static monnef.core.client.ResourcePathHelper.ResourceTextureType.ENTITY;
import static monnef.jaffas.food.JaffasFood.getItem;
import static monnef.jaffas.food.item.JaffaItem.spiderLegRaw;

public class EntityLittleSpider extends EntityJaffaSpider {
    public static final Item spiderMeat = getItem(spiderLegRaw);
    private static final String AGGRESSIVE_TIMER_TAG = "aggressiveTimer";
    private static final String WEB_TIMER_TAG = "webTimer";
    private static final String COLOR_TAG = "spiderColor";
    private static final int COLOR_WATCHER_INDEX = 25;
    public static final int COLORS_COUNT = 3;
    public static final String TEXTURE_STRING = "jaffas_littleSpider_%d.png";
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
        String name = String.format(TEXTURE_STRING, color);
        return ResourcePathHelper.assemble(name, ENTITY);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURES[getColor()];
    }


    public EntityLittleSpider(World world) {
        super(world);
        this.setSize(0.5F, 0.25F);
        this.timeUntilNextWeb = rand.nextInt(1500) + 500;
        if (MonnefCorePlugin.debugEnv) this.timeUntilNextWeb = 100;
        this.renderDistanceWeight = 2;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.8D);
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
        super.processAdditionalDrops(killedByPlayer, lootingLevel);
        if (killedByPlayer && (this.rand.nextInt(2) == 0 || this.rand.nextInt(1 + lootingLevel) > 1)) {
            this.dropItem(Items.string, 1);
        }
    }

    public static class MFR implements IFactoryGrindable {
        @Override
        public Class<? extends EntityLivingBase> getGrindableEntity() {
            return EntityLittleSpider.class;
        }

        @Override
        public List<MobDrop> grind(World world, EntityLivingBase entity, Random random) {
            ArrayList<MobDrop> res = new ArrayList<MobDrop>();
            res.add(new MobDrop(3, new ItemStack(spiderMeat, 1, 0)));
            res.add(new MobDrop(2, new ItemStack(Items.string, 1, 0)));
            return res;
        }

        @Override
        public boolean processEntity(EntityLivingBase entity) {
            return true;
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

    public static boolean isWeb(Block block) {
        return SpecialCobWebRegistry.blockRegistered(block);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (isAggressive()) {
            aggressiveTime -= 1;
        }

        if (!isChild() && !worldObj.isRemote && !isAggressive() && --timeUntilNextWeb <= 0) {
            int x = (int) Math.round(posX);
            int y = (int) Math.round(posY);
            int z = (int) Math.round(posZ);
            if (!worldObj.isAirBlock(x, y, z) && isWeb(worldObj.getBlock(x, y, z))) {
                y++;
                if (!worldObj.isAirBlock(x, y, z) && isWeb(worldObj.getBlock(x, y, z)) && rand.nextInt(2) == 0) {
                    y++;
                }
            }
            if (worldObj.isAirBlock(x, y, z)) {
                setNewTimeUntilNextWeb();
                Seq<Tuple2<CobWebDescriptor, Object>> scoreTable = CobWebInfluencingBlocksCalculator.calculateScoreTable(this);
                Block webBlock = RandomHelper.randomlySelectOne(scoreTable).block().apply();
                BlockHelper.setBlock(worldObj, x, y, z, webBlock);
                playWebSound();
            } else {
                // failed lookup for free space, trying again after some time
                timeUntilNextWeb = rand.nextInt(20 * 60 * 1) + 20 * 30;
                if (MonnefCorePlugin.debugEnv) timeUntilNextWeb = 20;
            }
        }
    }

    private void setNewTimeUntilNextWeb() {
        timeUntilNextWeb = rand.nextInt(4000) + 4000 + calculateBadEnvironmentPenaltyTicks(); // ~5m
        if (MonnefCorePlugin.debugEnv) {
            timeUntilNextWeb /= 100;
            MonnefCorePlugin.Log.printInfo(String.format("timeUntilNextWeb for spider @ %f %f %f is now set to %d (in production *100)", posX, posY, posZ, timeUntilNextWeb));
        }
    }

    private int calculateBadEnvironmentPenaltyTicks() {
        int x = (int) Math.round(posX);
        int y = (int) Math.round(posY);
        int z = (int) Math.round(posZ);
        int penalty = 0;

        if (worldObj.getFullBlockLightValue(x, y, z) >= 13) penalty += 1000 + rand.nextInt(1000);

        int collideableEntitiesInRange = 0;
        List<Entity> nearEntities = (List<Entity>) worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY, posZ).expand(3, 1, 3));
        for (Entity entity : nearEntities) {
            if (entity.canBeCollidedWith() || entity.getCollisionBox(this) != null) collideableEntitiesInRange++;
        }
        penalty += Math.round(20 * 10 * collideableEntitiesInRange * (1 + collideableEntitiesInRange / 5f) * (0.5f + rand.nextFloat()));

        return penalty;
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
