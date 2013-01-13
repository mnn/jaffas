package monnef.jaffas.food.client;

// Date: 2.12.2012 16:17:11
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSink extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape2;
    ModelRenderer Shape3x;
    ModelRenderer Shape5;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape8;
    ModelRenderer Shape9;
    ModelRenderer Shape10;
    ModelRenderer Shape11;
    ModelRenderer Shape12;
    ModelRenderer Shape13;

    public ModelSink() {
        textureWidth = 128;
        textureHeight = 64;

        Shape1 = new ModelRenderer(this, 31, 18);
        Shape1.addBox(0F, 0F, 0F, 16, 16, 1);
        Shape1.setRotationPoint(-8F, 8F, 7F);
        Shape1.setTextureSize(128, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 50, 0);
        Shape6.addBox(0F, 0F, 0F, 16, 16, 1);
        Shape6.setRotationPoint(-8F, 8F, -8F);
        Shape6.setTextureSize(128, 64);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 84, 0);
        Shape7.addBox(0F, 0F, 0F, 1, 16, 14);
        Shape7.setRotationPoint(-8F, 8F, -7F);
        Shape7.setTextureSize(128, 64);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 0);
        Shape2.addBox(0F, 0F, 0F, 1, 16, 14);
        Shape2.setRotationPoint(7F, 8F, -7F);
        Shape2.setTextureSize(128, 64);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3x = new ModelRenderer(this, 31, 49);
        Shape3x.addBox(0F, 0F, 0F, 5, 1, 14);
        Shape3x.setRotationPoint(-7F, 8F, -7F);
        Shape3x.setTextureSize(128, 64);
        Shape3x.mirror = true;
        setRotation(Shape3x, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 0, 31);
        Shape5.addBox(0F, 0F, 0F, 1, 4, 14);
        Shape5.setRotationPoint(-3F, 9F, -7F);
        Shape5.setTextureSize(128, 64);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 74, 31);
        Shape3.addBox(0F, 0F, 0F, 9, 1, 14);
        Shape3.setRotationPoint(-2F, 13F, -7F);
        Shape3.setTextureSize(128, 64);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 31, 36);
        Shape4.addBox(0F, 0F, 0F, 2, 8, 2);
        Shape4.setRotationPoint(-4F, 0F, -1F);
        Shape4.setTextureSize(128, 64);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 41, 36);
        Shape8.addBox(0F, 0F, 0F, 6, 2, 2);
        Shape8.setRotationPoint(-2F, 0F, -1F);
        Shape8.setTextureSize(128, 64);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(this, 40, 41);
        Shape9.addBox(0F, 0F, 0F, 2, 2, 2);
        Shape9.setRotationPoint(2F, 2F, -1F);
        Shape9.setTextureSize(128, 64);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 0F);
        Shape10 = new ModelRenderer(this, 58, 36);
        Shape10.addBox(0F, 0F, 0F, 1, 1, 2);
        Shape10.setRotationPoint(-4F, -1F, -1F);
        Shape10.setTextureSize(128, 64);
        Shape10.mirror = true;
        setRotation(Shape10, 0F, 0F, 0F);
        Shape11 = new ModelRenderer(this, 51, 41);
        Shape11.addBox(0F, 0F, 0F, 3, 1, 2);
        Shape11.setRotationPoint(-3F, -2F, -1F);
        Shape11.setTextureSize(128, 64);
        Shape11.mirror = true;
        setRotation(Shape11, 0F, 0F, 0F);
        Shape12 = new ModelRenderer(this, 74, 48);
        Shape12.addBox(0F, 5F, 0F, 9, 2, 14);
        Shape12.setRotationPoint(-2F, 6F, -7F);
        Shape12.setTextureSize(128, 64);
        Shape12.mirror = true;
        setRotation(Shape12, 0F, 0F, 0F);
        Shape13 = new ModelRenderer(this, 34, 2);
        Shape13.addBox(0F, 0F, 0F, 14, 1, 14);
        Shape13.setRotationPoint(-7F, 23F, -7F);
        Shape13.setTextureSize(128, 64);
        Shape13.mirror = true;
        setRotation(Shape13, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5, false);
    }

    public void render(float f5, boolean renderWater) {
        Shape1.render(f5);
        Shape6.render(f5);
        Shape7.render(f5);
        Shape2.render(f5);
        Shape3x.render(f5);
        Shape5.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape8.render(f5);
        Shape9.render(f5);
        Shape10.render(f5);
        Shape11.render(f5);
        if (renderWater) Shape12.render(f5);
        Shape13.render(f5);
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
