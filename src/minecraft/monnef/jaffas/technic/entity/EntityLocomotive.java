/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.entity;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static monnef.core.utils.MathHelper.degToRad;

public class EntityLocomotive extends EntityMinecart {
    public static final String REVERSE_TAG_NAME = "isInReverse";

    public EntityLocomotive(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
        this.setSize(0.98F, 1.7F);
        this.yOffset = this.height / 2.0F;
        this.renderDistanceWeight = 3;
    }

    public EntityLocomotive(World w) {
        super(w);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setDouble("PushX", this.pushX);
        tag.setDouble("PushZ", this.pushZ);
        tag.setShort("Fuel", (short) this.fuel);
        tag.setBoolean(REVERSE_TAG_NAME, isInReverse);
    }

    @Override
    public int getMinecartType() {
        return 297;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.pushX = tag.getDouble("PushX");
        this.pushZ = tag.getDouble("PushZ");
        this.fuel = tag.getShort("Fuel");
        isInReverse = tag.getBoolean(REVERSE_TAG_NAME);
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer) {
        fillFuel(par1EntityPlayer);
        return true;
    }

    private void fillFuel(EntityPlayer par1EntityPlayer) {
        fuel += 1000;
        float rot = degToRad(rotationYaw);
        this.pushX = this.posX - -MathHelper.cos(rot);
        this.pushZ = this.posZ - -MathHelper.sin(rot);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.fuel > 0) {
            --this.fuel;
        }

        if (this.fuel <= 0) {
            this.pushX = this.pushZ = 0.0D;
        }

        this.setMinecartPowered(this.fuel > 0);

        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
            this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (isMinecartPowered() && rand.nextInt(5) == 0 && getClass() == EntityLocomotive.class) {
            float x, z;
            float rot = degToRad(rotationYaw);

            x = -MathHelper.cos(rot);
            z = -MathHelper.sin(rot);
            float r = 0.6f;

            for (int i = 0; i < 2; i++) {
                worldObj.spawnParticle("largesmoke", posX + x * r, posY + 1.2D, posZ + z * r, 0.0D, rand.nextInt(2) == 0 ? 0.001D : 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean isPoweredCart() {
        return getClass() == EntityLocomotive.class;
    }

    public boolean isOnRails() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);

        if (BlockRail.isRailBlockAt(this.worldObj, x, y - 1, z)) {
            return true;
            //--y;
        }

        return false;
    }

    private int fuel = 0;
    public double pushX;
    public double pushZ;

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    @Override
    public void killMinecart(DamageSource par1DamageSource) {
        this.setDead();
        this.entityDropItem(new ItemStack(JaffasTechnic.itemLocomotive, 1), 0.0F);
    }

    @Override
    protected void updateOnTrack(int par1, int par2, int par3, double par4, double par6, int par8, int par9) {
        super.updateOnTrack(par1, par2, par3, par4, par6, par8, par9);
        double d2 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d2 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
            d2 = (double) MathHelper.sqrt_double(d2);
            this.pushX /= d2;
            this.pushZ /= d2;

            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
                this.pushX = 0.0D;
                this.pushZ = 0.0D;
            } else {
                this.pushX = this.motionX;
                this.pushZ = this.motionZ;
            }
        }
    }

    @Override
    protected void applyDrag() {
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d0 > 1.0E-4D) {
            d0 = (double) MathHelper.sqrt_double(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            double d1 = 0.05D;
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.800000011920929D;
            this.motionX += this.pushX * d1;
            this.motionZ += this.pushZ * d1;
        } else {
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9800000190734863D;
        }

        super.applyDrag();
    }

    protected boolean isMinecartPowered() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    protected void setMinecartPowered(boolean par1) {
        if (par1) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (this.dataWatcher.getWatchableObjectByte(16) | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (this.dataWatcher.getWatchableObjectByte(16) & -2)));
        }
    }

    @Override
    public Block getDefaultDisplayTile() {
        return Block.furnaceBurning;
    }

    @Override
    public int getDefaultDisplayTileData() {
        return 2;
    }
}
