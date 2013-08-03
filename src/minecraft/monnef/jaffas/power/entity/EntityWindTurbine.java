/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.entity;

import monnef.core.utils.BoxHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class EntityWindTurbine extends Entity {
    private AxisAlignedBB myBoundingBox;
    private ForgeDirection rotation = ForgeDirection.NORTH;
    private double radius = 3;

    public EntityWindTurbine(World world) {
        super(world);
        createBoundingBox();
    }

    private void createBoundingBox() {
        myBoundingBox = BoxHelper.createSquareFromCenter(radius, rotation);
        BoxHelper.narrowFrontSide(myBoundingBox, rotation, 0.5D);
        boundingBox.setBB(myBoundingBox);
        width = 1;
        height = 1;
    }

    public void configure(ForgeDirection rotation, double radius) {
        this.rotation = rotation;
        this.radius = radius;
        createBoundingBox();
    }

    @Override
    protected void entityInit() {
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
        setDead();
        return false;
    }

    // solid to player
    @Override
    public AxisAlignedBB getBoundingBox() {
        return myBoundingBox;
    }

    // attack stuff?
    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return myBoundingBox;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
}
