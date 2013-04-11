/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.technic.client;

import monnef.jaffas.technic.entity.EntityLocomotive;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderLocomotive extends Render {
    private final ModelBase model;

    public RenderLocomotive() {
        this.shadowSize = 0.5F;
        this.model = new ModelLocomotive();
    }

    public void renderTheMinecart(EntityMinecart entity, double par2, double par4, double par6, float renderRotation, float par9) {
        GL11.glPushMatrix();
        long var10 = (long) entity.entityId * 493286711L;
        var10 = var10 * var10 * 4392167121L + var10 * 98761L;
        float var12 = (((float) (var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float var13 = (((float) (var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float var14 = (((float) (var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GL11.glTranslatef(var12, var13, var14);
        double var15 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) par9;
        double var17 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) par9;
        double var19 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) par9;
        double var21 = 0.30000001192092896D;
        Vec3 var23 = entity.func_70489_a(var15, var17, var19);
        float tilt = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;

        if (var23 != null) {
            Vec3 var25 = entity.func_70495_a(var15, var17, var19, var21);
            Vec3 var26 = entity.func_70495_a(var15, var17, var19, -var21);

            if (var25 == null) {
                var25 = var23;
            }

            if (var26 == null) {
                var26 = var23;
            }

            par2 += var23.xCoord - var15;
            par4 += (var25.yCoord + var26.yCoord) / 2.0D - var17;
            par6 += var23.zCoord - var19;
            Vec3 var27 = var26.addVector(-var25.xCoord, -var25.yCoord, -var25.zCoord);

            if (var27.lengthVector() != 0.0D) {
                var27 = var27.normalize();
                renderRotation = (float) (Math.atan2(var27.zCoord, var27.xCoord) * 180.0D / Math.PI);
                tilt = (float) (Math.atan(var27.yCoord) * 73.0D);
            }
        }

        GL11.glTranslatef((float) par2, (float) par4, (float) par6);

        // rotation fix based on notes from covertJaguar
        double yaw = entity.rotationYaw;
        if (yaw < 0) yaw += 360;
        double rYaw = renderRotation;
        if (rYaw < 0) rYaw += 360;
        boolean applyRotateFix = false;

        if (Math.abs(rYaw - yaw) > 100) {
            applyRotateFix = true;
        }

        if (applyRotateFix) {
            renderRotation += 180;
            tilt = -tilt;
        }

        GL11.glRotatef(180.0F - renderRotation, 0.0F, 1.0F, 0.0F);

        GL11.glRotatef(-tilt, 0.0F, 0.0F, 1.0F);
        float var28 = (float) entity.getRollingAmplitude() - par9;
        float var30 = (float) entity.getDamage() - par9;

        if (var30 < 0.0F) {
            var30 = 0.0F;
        }

        if (var28 > 0.0F) {
            GL11.glRotatef(MathHelper.sin(var28) * var28 * var30 / 10.0F * (float) entity.getRollingDirection(), 1.0F, 0.0F, 0.0F);
        }

        /*
        if (entity.minecartType != 0) {
            this.loadTexture("/terrain.png");
            float var29 = 0.75F;
            GL11.glScalef(var29, var29, var29);

            if (entity.minecartType == 1) {
                GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                (new RenderBlocks()).renderBlockAsItem(Block.chest, 0, entity.getBrightness(par9));
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.5F, 0.0F, -0.5F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            } else if (entity.minecartType == 2) {
                GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
                (new RenderBlocks()).renderBlockAsItem(Block.stoneOvenIdle, 0, entity.getBrightness(par9));
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GL11.glScalef(1.0F / var29, 1.0F / var29, 1.0F / var29);
        }
        */


        this.loadTexture("/jaffas_locomotive.png");
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glTranslatef(0F, -1F, 0F);
        this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }


    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderTheMinecart((EntityLocomotive) par1Entity, par2, par4, par6, par8, par9);
    }
}
