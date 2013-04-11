/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.client;// Date: 25.1.2013 11:52:38
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPie extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;

    public ModelPie() {
        textureWidth = 64;
        textureHeight = 32;

        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 5, 3, 5);
        Shape1.setRotationPoint(-5F, 21F, -5F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 21, 10);
        Shape2.addBox(0F, 0F, 0F, 5, 3, 5);
        Shape2.setRotationPoint(0F, 21F, -5F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 10);
        Shape3.addBox(0F, 0F, 0F, 5, 3, 5);
        Shape3.setRotationPoint(0F, 21F, 0F);
        Shape3.setTextureSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 21, 0);
        Shape4.addBox(0F, 0F, 0F, 5, 3, 5);
        Shape4.setRotationPoint(-5F, 21F, 0F);
        Shape4.setTextureSize(64, 32);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5, new boolean[]{true, true, true, true});
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    }

    public void render(float f5, boolean[] pieces) {
        if (pieces[0]) Shape1.render(f5);
        if (pieces[1]) Shape2.render(f5);
        if (pieces[2]) Shape3.render(f5);
        if (pieces[3]) Shape4.render(f5);
    }
}
