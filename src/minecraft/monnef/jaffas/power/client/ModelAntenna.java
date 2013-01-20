package monnef.jaffas.power.client;

// Date: 19.1.2013 23:45:25
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAntenna extends ModelBase {

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
    ModelRenderer Shape10;
    ModelRenderer Shape11;
    ModelRenderer Shape12;
    ModelRenderer Shape13;
    ModelRenderer Shape14;
    ModelRenderer Shape15;
    ModelRenderer lit;
    ModelRenderer Shape17;
    ModelRenderer unlit;

    public ModelAntenna() {
        textureWidth = 128;
        textureHeight = 256;

        Shape1 = new ModelRenderer(this, 0, 16);
        Shape1.addBox(0F, 0F, 0F, 2, 4, 2);
        Shape1.setRotationPoint(-1F, 16F, -1F);
        Shape1.setTextureSize(128, 256);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 0);
        Shape2.addBox(0F, 0F, 0F, 6, 1, 6);
        Shape2.setRotationPoint(-3F, 15F, -3F);
        Shape2.setTextureSize(128, 256);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 13, 30);
        Shape3.addBox(0F, 0F, 0F, 2, 1, 6);
        Shape3.setRotationPoint(3F, 14F, -3F);
        Shape3.setTextureSize(128, 256);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 14, 11);
        Shape4.addBox(0F, 0F, 0F, 2, 1, 6);
        Shape4.setRotationPoint(-5F, 14F, -3F);
        Shape4.setTextureSize(128, 256);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 31, 26);
        Shape5.addBox(0F, 0F, 0F, 6, 1, 2);
        Shape5.setRotationPoint(-3F, 14F, -5F);
        Shape5.setTextureSize(128, 256);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 32, 13);
        Shape6.addBox(0F, 0F, 0F, 6, 1, 2);
        Shape6.setRotationPoint(-3F, 14F, 3F);
        Shape6.setTextureSize(128, 256);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 42, 0);
        Shape7.addBox(0F, 0F, 0F, 1, 6, 1);
        Shape7.setRotationPoint(-0.5F, 9F, -0.5F);
        Shape7.setTextureSize(128, 256);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 34, 34);
        Shape8.addBox(0F, 0F, 0F, 6, 1, 1);
        Shape8.setRotationPoint(-3F, 13F, -5F);
        Shape8.setTextureSize(128, 256);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(this, 30, 20);
        Shape9.addBox(0F, 0F, 0F, 6, 1, 1);
        Shape9.setRotationPoint(-3F, 13F, 4F);
        Shape9.setTextureSize(128, 256);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 0F);
        Shape10 = new ModelRenderer(this, 13, 20);
        Shape10.addBox(0F, 0F, 0F, 1, 1, 6);
        Shape10.setRotationPoint(-5F, 13F, -3F);
        Shape10.setTextureSize(128, 256);
        Shape10.mirror = true;
        setRotation(Shape10, 0F, 0F, 0F);
        Shape11 = new ModelRenderer(this, 13, 40);
        Shape11.addBox(0F, 0F, 0F, 1, 1, 6);
        Shape11.setRotationPoint(4F, 13F, -3F);
        Shape11.setTextureSize(128, 256);
        Shape11.mirror = true;
        setRotation(Shape11, 0F, 0F, 0F);
        Shape12 = new ModelRenderer(this, 51, 6);
        Shape12.addBox(0F, 0F, 0F, 1, 2, 1);
        Shape12.setRotationPoint(3F, 13F, 3F);
        Shape12.setTextureSize(128, 256);
        Shape12.mirror = true;
        setRotation(Shape12, 0F, 0F, 0F);
        Shape13 = new ModelRenderer(this, 58, 0);
        Shape13.addBox(0F, 0F, 0F, 1, 2, 1);
        Shape13.setRotationPoint(3F, 13F, -4F);
        Shape13.setTextureSize(128, 256);
        Shape13.mirror = true;
        setRotation(Shape13, 0F, 0F, 0F);
        Shape14 = new ModelRenderer(this, 51, 0);
        Shape14.addBox(0F, 0F, 0F, 1, 2, 1);
        Shape14.setRotationPoint(-4F, 13F, 3F);
        Shape14.setTextureSize(128, 256);
        Shape14.mirror = true;
        setRotation(Shape14, 0F, 0F, 0F);
        Shape15 = new ModelRenderer(this, 59, 6);
        Shape15.addBox(0F, 0F, 0F, 1, 2, 1);
        Shape15.setRotationPoint(-4F, 13F, -4F);
        Shape15.setTextureSize(128, 256);
        Shape15.mirror = true;
        setRotation(Shape15, 0F, 0F, 0F);
        lit = new ModelRenderer(this, 30, 0);
        lit.addBox(0F, 0F, 0F, 2, 2, 2);
        lit.setRotationPoint(-1F, 7F, -1F);
        lit.setTextureSize(128, 256);
        lit.mirror = true;
        setRotation(lit, 0F, 0F, 0F);
        Shape17 = new ModelRenderer(this, 0, 8);
        Shape17.addBox(0F, 0F, 0F, 2, 4, 2);
        Shape17.setRotationPoint(-1F, 20F, -1F);
        Shape17.setTextureSize(128, 256);
        Shape17.mirror = true;
        setRotation(Shape17, 0F, 0F, 0F);
        unlit = new ModelRenderer(this, 30, 6);
        unlit.addBox(0F, 0F, 0F, 2, 2, 2);
        unlit.setRotationPoint(-1F, 7F, -1F);
        unlit.setTextureSize(128, 256);
        unlit.mirror = true;
        setRotation(unlit, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5, false);
    }

    public void render(float f5, boolean burning) {
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape5.render(f5);
        Shape6.render(f5);
        Shape7.render(f5);
        Shape8.render(f5);
        Shape9.render(f5);
        Shape10.render(f5);
        Shape11.render(f5);
        Shape12.render(f5);
        Shape13.render(f5);
        Shape14.render(f5);
        Shape15.render(f5);
        Shape17.render(f5);
        if (burning) {
            lit.render(f5);
        } else {
            unlit.render(f5);
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