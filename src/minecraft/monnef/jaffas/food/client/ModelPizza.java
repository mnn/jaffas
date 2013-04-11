/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPizza extends ModelBase {
    //fields
    ModelRenderer dil11;
    ModelRenderer dil21;
    ModelRenderer dil31;
    ModelRenderer dil32;
    ModelRenderer dil22;
    ModelRenderer dil12;
    ModelRenderer dil41;
    ModelRenderer dil52;
    ModelRenderer dil61;
    ModelRenderer dil51;
    ModelRenderer dil42;
    ModelRenderer dil62;
    ModelRenderer dil33;
    ModelRenderer dil43;
    ModelRenderer dil63;
    ModelRenderer dil13;

    public ModelPizza() {
        textureWidth = 64;
        textureHeight = 32;

        dil11 = new ModelRenderer(this, 53, 6);
        dil11.addBox(0F, 0F, 0F, 1, 1, 4);
        dil11.setRotationPoint(-6F, 22.7F, -6F);
        dil11.setTextureSize(64, 32);
        dil11.mirror = true;
        setRotation(dil11, 0F, 0F, 0F);
        dil21 = new ModelRenderer(this, 38, 27);
        dil21.addBox(0F, 0F, 0F, 1, 1, 4);
        dil21.setRotationPoint(-6F, 22.7F, -2F);
        dil21.setTextureSize(64, 32);
        dil21.mirror = true;
        setRotation(dil21, 0F, 0F, 0F);
        dil31 = new ModelRenderer(this, 38, 21);
        dil31.addBox(0F, 0F, 0F, 1, 1, 4);
        dil31.setRotationPoint(-6F, 22.7F, 2F);
        dil31.setTextureSize(64, 32);
        dil31.mirror = true;
        setRotation(dil31, 0F, 0F, 0F);
        dil32 = new ModelRenderer(this, 19, 0);
        dil32.addBox(0F, 0F, 0F, 5, 1, 4);
        dil32.setRotationPoint(-5F, 23F, 2F);
        dil32.setTextureSize(64, 32);
        dil32.mirror = true;
        setRotation(dil32, 0F, 0F, 0F);
        dil22 = new ModelRenderer(this, 19, 6);
        dil22.addBox(0F, 0F, 0F, 5, 1, 4);
        dil22.setRotationPoint(-5F, 23F, -2F);
        dil22.setTextureSize(64, 32);
        dil22.mirror = true;
        setRotation(dil22, 0F, 0F, 0F);
        dil12 = new ModelRenderer(this, 19, 12);
        dil12.addBox(0F, 0F, 0F, 5, 1, 4);
        dil12.setRotationPoint(-5F, 23F, -6F);
        dil12.setTextureSize(64, 32);
        dil12.mirror = true;
        setRotation(dil12, 0F, 0F, 0F);
        dil41 = new ModelRenderer(this, 0, 18);
        dil41.addBox(0F, 0F, 0F, 5, 1, 4);
        dil41.setRotationPoint(0F, 23F, 2F);
        dil41.setTextureSize(64, 32);
        dil41.mirror = true;
        setRotation(dil41, 0F, 0F, 0F);
        dil52 = new ModelRenderer(this, 0, 12);
        dil52.addBox(0F, 0F, 0F, 5, 1, 4);
        dil52.setRotationPoint(0F, 23F, -2F);
        dil52.setTextureSize(64, 32);
        dil52.mirror = true;
        setRotation(dil52, 0F, 0F, 0F);
        dil61 = new ModelRenderer(this, 0, 6);
        dil61.addBox(0F, 0F, 0F, 5, 1, 4);
        dil61.setRotationPoint(0F, 23F, -6F);
        dil61.setTextureSize(64, 32);
        dil61.mirror = true;
        setRotation(dil61, 0F, 0F, 0F);
        dil51 = new ModelRenderer(this, 38, 3);
        dil51.addBox(0F, 0F, 0F, 1, 1, 4);
        dil51.setRotationPoint(5F, 22.7F, -2F);
        dil51.setTextureSize(64, 32);
        dil51.mirror = true;
        setRotation(dil51, 0F, 0F, 0F);
        dil42 = new ModelRenderer(this, 38, 9);
        dil42.addBox(0F, 0F, 0F, 1, 1, 4);
        dil42.setRotationPoint(5F, 22.7F, 2F);
        dil42.setTextureSize(64, 32);
        dil42.mirror = true;
        setRotation(dil42, 0F, 0F, 0F);
        dil62 = new ModelRenderer(this, 53, 0);
        dil62.addBox(0F, 0F, 0F, 1, 1, 4);
        dil62.setRotationPoint(5F, 22.7F, -6F);
        dil62.setTextureSize(64, 32);
        dil62.mirror = true;
        setRotation(dil62, 0F, 0F, 0F);
        dil33 = new ModelRenderer(this, 38, 18);
        dil33.addBox(0F, 0F, 0F, 6, 1, 1);
        dil33.setRotationPoint(-6F, 22.7F, 6F);
        dil33.setTextureSize(64, 32);
        dil33.mirror = true;
        setRotation(dil33, 0F, 0F, 0F);
        dil43 = new ModelRenderer(this, 38, 15);
        dil43.addBox(0F, 0F, 0F, 6, 1, 1);
        dil43.setRotationPoint(0F, 22.7F, 6F);
        dil43.setTextureSize(64, 32);
        dil43.mirror = true;
        setRotation(dil43, 0F, 0F, 0F);
        dil63 = new ModelRenderer(this, 38, 0);
        dil63.addBox(0F, 0F, 0F, 6, 1, 1);
        dil63.setRotationPoint(0F, 22.7F, -7F);
        dil63.setTextureSize(64, 32);
        dil63.mirror = true;
        setRotation(dil63, 0F, 0F, 0F);
        dil13 = new ModelRenderer(this, 49, 12);
        dil13.addBox(0F, 0F, 0F, 6, 1, 1);
        dil13.setRotationPoint(-6F, 22.7F, -7F);
        dil13.setTextureSize(64, 32);
        dil13.mirror = true;
        setRotation(dil13, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5, 6);
    }

    public void render(float f5, int slices) {
        if (slices > 5) {
            dil11.render(f5);
            dil12.render(f5);
            dil13.render(f5);
        }
        if (slices > 4) {
            dil21.render(f5);
            dil22.render(f5);
        }
        if (slices > 3) {
            dil31.render(f5);
            dil32.render(f5);
            dil33.render(f5);
        }
        if (slices > 2) {
            dil41.render(f5);
            dil42.render(f5);
            dil43.render(f5);
        }
        if (slices > 1) {
            dil51.render(f5);
            dil52.render(f5);
        }
        if (slices > 0) {
            dil61.render(f5);
            dil62.render(f5);
            dil63.render(f5);
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
