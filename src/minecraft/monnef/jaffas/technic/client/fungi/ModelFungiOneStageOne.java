/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client.fungi;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFungiOneStageOne extends ModelFungi {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;

    public ModelFungiOneStageOne() {
        textureWidth = 128;
        textureHeight = 32;

        Shape1 = new ModelRenderer(this, 20, 5);
        Shape1.addBox(0F, 0F, 0F, 1, 2, 1);
        Shape1.setRotationPoint(3.5F, 16.5F, -3F);
        Shape1.setTextureSize(128, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 20, 0);
        Shape2.addBox(0F, 0F, 0F, 2, 1, 2);
        Shape2.setRotationPoint(3F, 16.4F, -3.5F);
        Shape2.setTextureSize(128, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 10, 0);
        Shape3.addBox(0F, 0F, 0F, 2, 1, 2);
        Shape3.setRotationPoint(-0.5F, 16.4F, 2.5F);
        Shape3.setTextureSize(128, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 10, 4);
        Shape4.addBox(0F, 0F, 0F, 1, 2, 1);
        Shape4.setRotationPoint(0F, 16.5F, 3F);
        Shape4.setTextureSize(128, 32);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 0, 0);
        Shape5.addBox(0F, 0F, 0F, 2, 1, 2);
        Shape5.setRotationPoint(-4F, 16.4F, -4F);
        Shape5.setTextureSize(128, 32);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 0, 5);
        Shape6.addBox(0F, 0F, 0F, 1, 2, 1);
        Shape6.setRotationPoint(-3.5F, 16.5F, -3.5F);
        Shape6.setTextureSize(128, 32);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
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
    }

    @Override
    public String getTexture() {
        return "jaffas_fungi_01_01.png";
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

