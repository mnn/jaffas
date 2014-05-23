/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client.fungi;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFungiParasolStageThree extends ModelFungi {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;

    public ModelFungiParasolStageThree() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new ModelRenderer(this, 0, 16);
        Shape1.addBox(0F, 0F, 0F, 1, 8, 1);
        Shape1.setRotationPoint(3F, 10.5F, -2F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 8);
        Shape2.addBox(0F, 0F, 0F, 5, 1, 5);
        Shape2.setRotationPoint(1F, 9.5F, -4F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 0);
        Shape3.addBox(0F, 0F, 0F, 6, 1, 6);
        Shape3.setRotationPoint(0.5F, 9.3F, -4.5F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 6, 17);
        Shape4.addBox(0F, 0F, 0F, 2, 0, 2);
        Shape4.setRotationPoint(2.5F, 12.5F, -2.5F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 46, 15);
        Shape5.addBox(0F, 0F, 0F, 1, 8, 1);
        Shape5.setRotationPoint(-5F, 10.5F, 4F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 35, 16);
        Shape6.addBox(0F, 0F, 0F, 2, 0, 2);
        Shape6.setRotationPoint(-5.5F, 12.5F, 3.5F);
        Shape6.setTextureSize(128, 128);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 34, 0);
        Shape7.addBox(0F, 0F, 0F, 6, 1, 6);
        Shape7.setRotationPoint(-7.5F, 9.3F, 1F);
        Shape7.setTextureSize(128, 128);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 37, 8);
        Shape8.addBox(0F, 0F, 0F, 5, 1, 5);
        Shape8.setRotationPoint(-7F, 9.5F, 1.5F);
        Shape8.setTextureSize(128, 128);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5);
    }

    @Override
    public void render(float f5) {
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape5.render(f5);
        Shape6.render(f5);
        Shape7.render(f5);
        Shape8.render(f5);
    }

    @Override
    public String getTexture() {
        return "jaffas_fungi_02_03.png";
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

