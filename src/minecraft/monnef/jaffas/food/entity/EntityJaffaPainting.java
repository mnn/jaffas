/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.entity;

import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityJaffaPainting extends Entity {
    public static final int watcherByteNumber = 10;
    public static final int watcherNumberPosX = 11;
    public static final int watcherNumberPosY = 12;
    public static final int watcherNumberPosZ = 13;
    public static final int watcherNumberDirection = 14;
    private int tickCounter1;

    /**
     * the direction the painting faces
     */
    public int direction;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public EnumJaffaArt art;
    private boolean notSynced = true;

    public EntityJaffaPainting(World par1World) {
        super(par1World);
        this.tickCounter1 = 99;
        this.direction = 0;
        this.yOffset = 0.0F;
        this.setSize(0.5F, 0.5F);
    }

    public EntityJaffaPainting(World par1World, int par2, int par3, int par4, int par5) {
        this(par1World);
        this.xPosition = par2;
        this.yPosition = par3;
        this.zPosition = par4;
        ArrayList var6 = new ArrayList();
        EnumJaffaArt[] var7 = EnumJaffaArt.values();
        int var8 = var7.length;

        for (int var9 = 0; var9 < var8; ++var9) {
            EnumJaffaArt var10 = var7[var9];
            this.art = var10;
            this.setDirection(par5);

            if (this.onValidSurface()) {
                var6.add(var10);
            }
        }

        if (!var6.isEmpty()) {
            this.art = (EnumJaffaArt) var6.get(this.rand.nextInt(var6.size()));
        }

        this.updateWatcher();
        this.setDirection(par5);
    }

    /*
    @SideOnly(Side.CLIENT)
    public EntityJaffaPainting(World par1World, int par2, int par3, int par4, int par5, String par6Str) {
        this(par1World);
        this.xPosition = par2;
        this.yPosition = par3;
        this.zPosition = par4;
        EnumJaffaArt[] var7 = EnumJaffaArt.values();
        int var8 = var7.length;

        for (int var9 = 0; var9 < var8; ++var9) {
            EnumJaffaArt var10 = var7[var9];

            if (var10.title.equals(par6Str)) {
                this.art = var10;
                break;
            }
        }

        this.setDirection(par5);
    }
    */

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(watcherByteNumber, Integer.valueOf(0));
        this.dataWatcher.addObject(watcherNumberPosX, Integer.valueOf(this.xPosition));
        this.dataWatcher.addObject(watcherNumberPosY, Integer.valueOf(this.yPosition));
        this.dataWatcher.addObject(watcherNumberPosZ, Integer.valueOf(this.zPosition));
        this.dataWatcher.addObject(watcherNumberDirection, Integer.valueOf(this.direction));
    }

    /**
     * Sets the direction the painting faces.
     */
    public void setDirection(int par1) {
        this.direction = par1;
        this.prevRotationYaw = this.rotationYaw = (float) (par1 * 90);
        float var2 = (float) this.art.sizeX;
        float var3 = (float) this.art.sizeY;
        float var4 = (float) this.art.sizeX;

        if (par1 != 0 && par1 != 2) {
            var2 = 0.5F;
        } else {
            var4 = 0.5F;
        }

        var2 /= 32.0F;
        var3 /= 32.0F;
        var4 /= 32.0F;
        float var5 = (float) this.xPosition + 0.5F;
        float var6 = (float) this.yPosition + 0.5F;
        float var7 = (float) this.zPosition + 0.5F;
        float var8 = 0.5625F;

        if (par1 == 0) {
            var7 -= var8;
        }

        if (par1 == 1) {
            var5 -= var8;
        }

        if (par1 == 2) {
            var7 += var8;
        }

        if (par1 == 3) {
            var5 += var8;
        }

        if (par1 == 0) {
            var5 -= this.func_70517_b(this.art.sizeX);
        }

        if (par1 == 1) {
            var7 += this.func_70517_b(this.art.sizeX);
        }

        if (par1 == 2) {
            var5 += this.func_70517_b(this.art.sizeX);
        }

        if (par1 == 3) {
            var7 -= this.func_70517_b(this.art.sizeX);
        }

        var6 += this.func_70517_b(this.art.sizeY);
        this.setPosition((double) var5, (double) var6, (double) var7);
        float var9 = -0.00625F;
        this.boundingBox.setBounds((double) (var5 - var2 - var9), (double) (var6 - var3 - var9), (double) (var7 - var4 - var9), (double) (var5 + var2 + var9), (double) (var6 + var3 + var9), (double) (var7 + var4 + var9));
    }

    private float func_70517_b(int par1) {
        return par1 == 32 ? 0.5F : (par1 == 64 ? 0.5F : 0.0F);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        if (this.tickCounter1++ == 100) {
            this.tickCounter1 = 0;

            if (!this.worldObj.isRemote) {
                if (!this.isDead && !this.onValidSurface()) {
                    this.setDead();
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(JaffasFood.itemPainting)));
                }
            }

            updateWatcher();
        }
    }

    private void updateWatcher() {
        if (this.worldObj.isRemote) {
            EnumJaffaArt newArt = EnumJaffaArt.values()[this.dataWatcher.getWatchableObjectInt(watcherByteNumber)];

            if (newArt != this.art) this.art = newArt;
            if (this.notSynced) {
                this.xPosition = this.dataWatcher.getWatchableObjectInt(watcherNumberPosX);
                this.yPosition = this.dataWatcher.getWatchableObjectInt(watcherNumberPosY);
                this.zPosition = this.dataWatcher.getWatchableObjectInt(watcherNumberPosZ);
                this.direction = this.dataWatcher.getWatchableObjectInt(watcherNumberDirection);

                this.setDirection(this.direction);
                this.notSynced = false;
            }
        } else {
            this.dataWatcher.updateObject(watcherByteNumber, Integer.valueOf(this.art.ordinal()));

            this.dataWatcher.updateObject(watcherNumberPosX, Integer.valueOf(this.xPosition));
            this.dataWatcher.updateObject(watcherNumberPosY, Integer.valueOf(this.yPosition));
            this.dataWatcher.updateObject(watcherNumberPosZ, Integer.valueOf(this.zPosition));
            this.dataWatcher.updateObject(watcherNumberDirection, Integer.valueOf(this.direction));
        }
    }

    /**
     * checks to make sure painting can be placed there
     */
    public boolean onValidSurface() {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
            return false;
        } else {
            int var1 = this.art.sizeX / 16;
            int var2 = this.art.sizeY / 16;
            int var3 = this.xPosition;
            int var4 = this.yPosition;
            int var5 = this.zPosition;

            if (this.direction == 0) {
                var3 = MathHelper.floor_double(this.posX - (double) ((float) this.art.sizeX / 32.0F));
            }

            if (this.direction == 1) {
                var5 = MathHelper.floor_double(this.posZ - (double) ((float) this.art.sizeX / 32.0F));
            }

            if (this.direction == 2) {
                var3 = MathHelper.floor_double(this.posX - (double) ((float) this.art.sizeX / 32.0F));
            }

            if (this.direction == 3) {
                var5 = MathHelper.floor_double(this.posZ - (double) ((float) this.art.sizeX / 32.0F));
            }

            var4 = MathHelper.floor_double(this.posY - (double) ((float) this.art.sizeY / 32.0F));

            for (int var6 = 0; var6 < var1; ++var6) {
                for (int var7 = 0; var7 < var2; ++var7) {
                    Material var8;

                    if (this.direction != 0 && this.direction != 2) {
                        var8 = this.worldObj.getBlockMaterial(this.xPosition, var4 + var7, var5 + var6);
                    } else {
                        var8 = this.worldObj.getBlockMaterial(var3 + var6, var4 + var7, this.zPosition);
                    }

                    if (!var8.isSolid()) {
                        return false;
                    }
                }
            }

            List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);
            Iterator var10 = var9.iterator();
            Entity var11;

            do {
                if (!var10.hasNext()) {
                    return true;
                }

                var11 = (Entity) var10.next();
            }
            while (!(var11 instanceof EntityJaffaPainting));

            return false;
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
        if (!this.isDead && !this.worldObj.isRemote) {
            this.setDead();
            this.setBeenAttacked();
            EntityPlayer var3 = null;

            if (par1DamageSource.getEntity() instanceof EntityPlayer) {
                var3 = (EntityPlayer) par1DamageSource.getEntity();
            }

            if (var3 != null && var3.capabilities.isCreativeMode) {
                return true;
            }

            this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(JaffasFood.itemPainting)));
        }

        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setByte("Dir", (byte) this.direction);
        par1NBTTagCompound.setString("Motive", this.art.title);
        par1NBTTagCompound.setInteger("TileX", this.xPosition);
        par1NBTTagCompound.setInteger("TileY", this.yPosition);
        par1NBTTagCompound.setInteger("TileZ", this.zPosition);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        this.direction = par1NBTTagCompound.getByte("Dir");
        this.xPosition = par1NBTTagCompound.getInteger("TileX");
        this.yPosition = par1NBTTagCompound.getInteger("TileY");
        this.zPosition = par1NBTTagCompound.getInteger("TileZ");
        String var2 = par1NBTTagCompound.getString("Motive");
        EnumJaffaArt[] var3 = EnumJaffaArt.values();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            EnumJaffaArt var6 = var3[var5];

            if (var6.title.equals(var2)) {
                this.art = var6;
            }
        }

        if (this.art == null) {
            this.art = EnumJaffaArt.Kebab;
        }

        this.setDirection(this.direction);
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
    public void moveEntity(double par1, double par3, double par5) {
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D) {
            this.setDead();
            this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(JaffasFood.itemPainting)));
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    @Override
    public void addVelocity(double par1, double par3, double par5) {
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D) {
            this.setDead();
            this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(JaffasFood.itemPainting)));
        }
    }
}
