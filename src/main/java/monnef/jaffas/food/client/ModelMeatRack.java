/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMeatRack extends ModelBase {
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
    ModelRenderer Shape17;

    public ModelMeatRack() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new ModelRenderer(this, 36, 49);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 1);
        Shape1.setRotationPoint(-8F, 22F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 49);
        Shape2.addBox(0F, 0F, 0F, 16, 1, 1);
        Shape2.setRotationPoint(-8F, 22F, 7F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 63, 53);
        Shape3.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape3.setRotationPoint(7F, 23F, -8F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 35, 54);
        Shape4.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape4.setRotationPoint(-8F, 23F, -8F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 30, 54);
        Shape5.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape5.setRotationPoint(-8F, 23F, 7F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 0, 53);
        Shape6.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape6.setRotationPoint(7F, 23F, 7F);
        Shape6.setTextureSize(128, 128);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 63, 29);
        Shape7.addBox(0F, 0F, 0F, 1, 13, 1);
        Shape7.setRotationPoint(5F, 9F, -8F);
        Shape7.setTextureSize(128, 128);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 0, 28);
        Shape8.addBox(0F, 0F, 0F, 1, 13, 1);
        Shape8.setRotationPoint(5F, 9F, 7F);
        Shape8.setTextureSize(128, 128);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(this, 27, 28);
        Shape9.addBox(0F, 0F, 0F, 1, 13, 1);
        Shape9.setRotationPoint(-6F, 9F, 7F);
        Shape9.setTextureSize(128, 128);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 0F);
        Shape10 = new ModelRenderer(this, 35, 29);
        Shape10.addBox(0F, 0F, 0F, 1, 13, 1);
        Shape10.setRotationPoint(-6F, 9F, -8F);
        Shape10.setTextureSize(128, 128);
        Shape10.mirror = true;
        setRotation(Shape10, 0F, 0F, 0F);
        Shape11 = new ModelRenderer(this, 0, 3);
        Shape11.addBox(0F, 0F, 0F, 16, 1, 1);
        Shape11.setRotationPoint(-8F, 8F, 7F);
        Shape11.setTextureSize(128, 128);
        Shape11.mirror = true;
        setRotation(Shape11, 0F, 0F, 0F);
        Shape12 = new ModelRenderer(this, 0, 0);
        Shape12.addBox(0F, 0F, 0F, 16, 1, 1);
        Shape12.setRotationPoint(-8F, 8F, -8F);
        Shape12.setTextureSize(128, 128);
        Shape12.mirror = true;
        setRotation(Shape12, 0F, 0F, 0F);
        Shape13 = new ModelRenderer(this, 73, 0);
        Shape13.addBox(0F, 0F, 0F, 1, 1, 14);
        Shape13.setRotationPoint(-6F, 10F, -7F);
        Shape13.setTextureSize(128, 128);
        Shape13.mirror = true;
        setRotation(Shape13, 0F, 0F, 0F);
        Shape17 = new ModelRenderer(this, 38, 0);
        Shape17.addBox(0F, 0F, 0F, 1, 1, 14);
        Shape17.setRotationPoint(5F, 10F, -7F);
        Shape17.setTextureSize(128, 128);
        Shape17.mirror = true;
        setRotation(Shape17, 0F, 0F, 0F);
    }

    @Override
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
        Shape10.render(f5);
        Shape11.render(f5);
        Shape12.render(f5);
        Shape13.render(f5);
        Shape17.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    }
}
