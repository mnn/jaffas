/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.entity;

import monnef.core.utils.BoxHelper;
import monnef.core.utils.ColorHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.block.TileWindGenerator;
import monnef.jaffas.power.item.ItemWindTurbine;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class EntityWindTurbine extends Entity {
    private static final int SPEED_WID = 10;
    private static final int ROTATION_WID = 11;
    private static final int COLOR_WID = 12;
    private static final int ITEM_PROTOTYPE_WID = 13;
    public static final double BOUNDING_BOX_LENGTH = 0.5D;

    public float animationRotation = JaffasFood.rand.nextInt(10) / 10f;

    private AxisAlignedBB boundingBoxTemplate;
    private boolean initialized;
    private TileWindGenerator tileWindGenerator;

    public EntityWindTurbine(World world) {
        super(world);
    }

    private boolean createBoundingBoxTemplate() {
        int radius = getRadius();
        if (radius == 0) return false;
        boundingBoxTemplate = BoxHelper.createSquareFromCenter(radius, getTurbineRotation(), BOUNDING_BOX_LENGTH);
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
        tileWindGenerator.onEntityTurbineHit(damageSource, amount, this);
        return false;
    }


    // attack stuff?
    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return boundingBoxTemplate;
    }

    // player collision
    @Override
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (!initialized) {
            initialized = createBoundingBoxTemplate();
        } else {
            animationRotation += getItemPrototype().getRotationSpeedPerTick();
        }
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
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
}
