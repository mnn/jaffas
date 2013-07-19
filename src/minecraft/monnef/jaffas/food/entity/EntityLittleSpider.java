package monnef.jaffas.food.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.utils.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    private int timeUntilNextWeb;

    public EntityLittleSpider(World world) {
        super(world);
        this.texture = "/jaffas_littleSpider.png";
        //this.setSize(1.4F, 0.9F);
        this.setSize(0.5F, 0.25F);
        this.moveSpeed = 0.8F;
        this.timeUntilNextWeb = this.rand.nextInt(6000) + 6000;
        this.renderDistanceWeight = 2;
    }

    @Override
    public int getMaxHealth() {
        return 10;
    }

    @Override
    protected Entity findPlayerToAttack() {
        return null;
    }

    @Override
    public boolean isOnLadder() {
        return entityToAttack == null ? false : this.isBesideClimbableBlock();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float spiderScaleAmount() {
        return 0.4F;
    }

    @Override
    public void initCreature() {
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

        if (!this.isChild() && !this.worldObj.isRemote && --this.timeUntilNextWeb <= 0) {
            //this.playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
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
            } else {
                // failed lookup for free space, trying again after some time
                this.timeUntilNextWeb = 20 * 30;
            }
        }
    }

    @Override
    public boolean canSpawnWithSkeleton() {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }
}
