/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.entity;

import monnef.core.utils.BoxHelper;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.EntityHelper;
import monnef.core.utils.RandomHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.block.TileWindGenerator;
import monnef.jaffas.power.item.ItemWindTurbine;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class EntityWindTurbine extends Entity {
    private static final int SPEED_WID = 10;
    private static final int ROTATION_WID = 11;
    private static final int COLOR_WID = 12;
    private static final int ITEM_PROTOTYPE_WID = 13;
    public static final double BOUNDING_BOX_LENGTH = 0.5D;
    private static final int PLAYER_DAMAGE_MAX = 10;
    private static final int NO_DMG_SPEED_THRESHOLD = 10;

    public float animationRotation = JaffasFood.rand.nextInt(10) / 10f;

    private AxisAlignedBB boundingBoxTemplate;
    private boolean initialized;
    private TileWindGenerator tileWindGenerator;
    private int counter = JaffasFood.rand.nextInt();
    private int initFails = 0;
    private int damageCooldown = 0;

    public EntityWindTurbine(World world) {
        super(world);
        this.yOffset = 0.0F;
        noClip = true;
    }

    private boolean createBoundingBoxTemplate() {
        int radius = getRadius();
        if (radius == 0) return false;
        boundingBoxTemplate = BoxHelper.getNonPooledAABB(BoxHelper.createSquareFromCenter(radius, getTurbineRotation(), BOUNDING_BOX_LENGTH));
        if (boundingBoxTemplate == null) {
            throw new RuntimeException("Possible error in synchronization - got null square => wrong turbine rotation");
        }
        BoxHelper.narrowFrontSide(boundingBoxTemplate, getTurbineRotation(), BOUNDING_BOX_LENGTH / 2);
        width = 2 * radius + 1;
        height = 2 * radius + 1;
        recalculateBoundingBox();
        return true;
    }

    private void recalculateBoundingBox() {
        boundingBox.setBB(boundingBoxTemplate.getOffsetBoundingBox(posX, posY, posZ));
    }

    public ForgeDirection getTurbineRotation() {
        return ForgeDirection.getOrientation(dataWatcher.getWatchableObjectByte(ROTATION_WID));
    }

    public int getRadius() {
        ItemWindTurbine item = getItemPrototype();
        return item != null ? item.getRadius() : 0;
    }

    public ItemWindTurbine getItemPrototype() {
        int id = dataWatcher.getWatchableObjectInt(ITEM_PROTOTYPE_WID);
        if (id < 0) return null;
        return (ItemWindTurbine) Item.itemsList[id];
    }

    public void configure(ForgeDirection rotation, ItemStack stack, TileWindGenerator tileWindGenerator) {
        this.tileWindGenerator = tileWindGenerator;
        setTurbineRotations(rotation);
        ItemWindTurbine item = (ItemWindTurbine) stack.getItem();
        setItemPrototype(item);
        setColor(item.getTurbineColor(stack));
        //createBoundingBoxTemplate();
    }

    private void setColor(int color) {
        dataWatcher.updateObject(COLOR_WID, color);
    }

    public int getColor() {
        return dataWatcher.getWatchableObjectInt(COLOR_WID);
    }

    private void setItemPrototype(ItemWindTurbine item) {
        dataWatcher.updateObject(ITEM_PROTOTYPE_WID, item != null ? item.itemID : 0);
    }

    private void setTurbineRotations(ForgeDirection rotation) {
        dataWatcher.updateObject(ROTATION_WID, (byte) rotation.ordinal());
    }

    public void updateStatus(int speed) {
        setSpeed(speed);
    }

    private void setSpeed(int speed) {
        dataWatcher.updateObject(SPEED_WID, speed);
    }

    private int getSpeed() {
        return dataWatcher.getWatchableObjectInt(SPEED_WID);
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(SPEED_WID, 0);
        dataWatcher.addObject(ROTATION_WID, (byte) ForgeDirection.UNKNOWN.ordinal());
        dataWatcher.addObject(COLOR_WID, ColorHelper.WHITE_INT);
        dataWatcher.addObject(ITEM_PROTOTYPE_WID, 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, int amount) {
        super.attackEntityFrom(damageSource, amount);
        if (tileWindGenerator != null) {
            tileWindGenerator.onEntityTurbineHit(damageSource, amount, this);
        }
        return false;
    }

    // attack stuff?
    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return boundingBox;
    }

    // player collision
    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
        //return boundingBox;
    }

    @Override
    public void onEntityUpdate() {
        //super.onEntityUpdate();
        if (!initialized) {
            initialized = createBoundingBoxTemplate();
            if (!initialized) {
                initFails++;
                if (initFails > 5) {
                    JaffasFood.Log.printDebug(this.getClass().getSimpleName() + ": too many init failures, dying.");
                    breakTurbine();
                }
            }
        } else {
            animationRotation += getCurrentRotationPerTick();
            if (animationRotation > 1) animationRotation -= 1;
            counter++;
            if (counter % 20 == 0) {
                checkBlockCollision();
            }
            if (counter % 5 == 0) {
                if (damageCooldown > 0) damageCooldown--;
                if (damageCooldown == 0) checkEntityCollision();
            }
        }
    }

    private void checkEntityCollision() {
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2D, 0.0D, 0.2D));

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity instanceof EntityLiving) {
                    this.onCollideWithEntity((EntityLiving) entity);
                }
            }
        }
    }

    public void onCollideWithEntity(EntityLiving entity) {
        if (damageCooldown > 0) return;
        if (!worldObj.isRemote) {
            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) return;
            int damage = (getSpeed() * PLAYER_DAMAGE_MAX) / TileWindGenerator.TURBINE_MAX_SPEED;
            if (getSpeed() < NO_DMG_SPEED_THRESHOLD) damage = 0;
            if (damage > 0) {
                if (JaffasFood.rand.nextInt(3) == 0) {
                    entity.setEntityHealth(entity.getHealth() - damage);
                } else {
                    entity.attackEntityFrom(DamageSource.generic, damage);
                }
                float force = RandomHelper.generateRandomFromInterval(0.2f, 0.9f) * ((float) getSpeed() / TileWindGenerator.TURBINE_MAX_SPEED);
                ForgeDirection forceDirection = JaffasFood.rand.nextBoolean() ? getTurbineRotation() : getTurbineRotation().getOpposite();
                EntityHelper.kickEntityInDirection(entity, forceDirection, force);
                entity.addVelocity(0, force / 3, 0);
                damageCooldown = 20;
            }
        }
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        if (initialized) {
            recalculateBoundingBox();
        }
    }

    @Override
    protected void setSize(float width, float height) {
        super.setSize(width, height);
        if (initialized) {
            recalculateBoundingBox();
        }
    }

    public int getModelId() {
        ItemWindTurbine prototype = getItemPrototype();
        return prototype != null ? prototype.getModel() : 0;
    }

    @Override
    public void setDead() {
        if (worldObj.isRemote) {
            JaffasFood.Log.printDebug(this.getClass().getSimpleName() + ": dying on client");
        }
        super.setDead();
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D) {
            this.breakTurbine();
        }
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D) {
            this.breakTurbine();
        }
    }

    public void checkBlockCollision() {
        if (worldObj.isRemote) return;
        int x1 = MathHelper.floor_double(this.boundingBox.minX + 0.001D);
        int y1 = MathHelper.floor_double(this.boundingBox.minY + 0.001D);
        int z1 = MathHelper.floor_double(this.boundingBox.minZ + 0.001D);
        int x2 = MathHelper.floor_double(this.boundingBox.maxX - 0.001D);
        int y2 = MathHelper.floor_double(this.boundingBox.maxY - 0.001D);
        int z2 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001D);

        if (this.worldObj.checkChunksExist(x1, y1, z1, x2, y2, z2)) {
            for (int xx = x1; xx <= x2; ++xx) {
                for (int yy = y1; yy <= y2; ++yy) {
                    for (int zz = z1; zz <= z2; ++zz) {
                        int blockId = this.worldObj.getBlockId(xx, yy, zz);
                        if (blockId != 0) {
                            breakTurbine();
                        }
                    }
                }
            }
        }
    }

    public void breakTurbine() {
        this.setDead();
        if (tileWindGenerator != null) {
            tileWindGenerator.onEntityTurbineDeath(this);
        }
    }

    public float getCurrentRotationPerTick() {
        return getItemPrototype().getRotationSpeedPerTick() * ((float) getSpeed() / TileWindGenerator.TURBINE_MAX_SPEED);
    }

    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        // to prevent spazzing when player is colliding
    }
}
