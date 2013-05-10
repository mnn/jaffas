/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelJaffaStatue extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;

    public ModelJaffaStatue() {
        textureWidth = 256;
        textureHeight = 128;

        Shape1 = new ModelRenderer(this, 0, 74);
        Shape1.addBox(0F, 0F, 0F, 12, 8, 1);
        Shape1.setRotationPoint(-6F, 12F, -2F);
        Shape1.setTextureSize(256, 128);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 91);
        Shape2.addBox(0F, 0F, 0F, 8, 2, 1);
        Shape2.setRotationPoint(-4F, 20F, -2F);
        Shape2.setTextureSize(256, 128);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 80, 0);
        Shape3.addBox(0F, 0F, 0F, 16, 12, 2);
        Shape3.setRotationPoint(-8F, 10F, -1F);
        Shape3.setTextureSize(256, 128);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 78, 16);
        Shape4.addBox(0F, 0F, 0F, 12, 2, 2);
        Shape4.setRotationPoint(-6F, 8F, -1F);
        Shape4.setTextureSize(256, 128);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 78, 26);
        Shape5.addBox(0F, 0F, 0F, 12, 2, 2);
        Shape5.setRotationPoint(-6F, 22F, -1F);
        Shape5.setTextureSize(256, 128);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 125, 11);
        Shape6.addBox(0F, 0F, 0F, 14, 10, 2);
        Shape6.setRotationPoint(-7F, 11F, 1F);
        Shape6.setTextureSize(256, 128);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 124, 0);
        Shape7.addBox(0F, 0F, 0F, 10, 2, 2);
        Shape7.setRotationPoint(-5F, 9F, 1F);
        Shape7.setTextureSize(256, 128);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 125, 6);
        Shape8.addBox(0F, 0F, 0F, 10, 2, 2);
        Shape8.setRotationPoint(-5F, 21F, 1F);
        Shape8.setTextureSize(256, 128);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(this, 0, 86);
        Shape9.addBox(0F, 0F, 0F, 8, 2, 1);
        Shape9.setRotationPoint(-4F, 10F, -2F);
        Shape9.setTextureSize(256, 128);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5);
    }

    public void render(float f5) {
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape5.render(f5);
        Shape6.render(f5);
        Shape7.render(f5);
        Shape8.render(f5);
        Shape9.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    }

}
