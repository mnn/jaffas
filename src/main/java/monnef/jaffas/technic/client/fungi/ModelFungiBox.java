// Date: 24.5.2013 17:48:26
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package monnef.jaffas.technic.client.fungi;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelFungiBox extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;

    public ModelFungiBox() {
        textureWidth = 128;
        textureHeight = 64;

        Shape1 = new ModelRenderer(this, 31, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 6, 1);
        Shape1.setRotationPoint(-8F, 18F, -8F);
        Shape1.setTextureSize(128, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 31, 10);
        Shape2.addBox(0F, 0F, 0F, 16, 6, 1);
        Shape2.setRotationPoint(-8F, 18F, 7F);
        Shape2.setTextureSize(128, 64);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 0);
        Shape3.addBox(0F, 0F, 0F, 1, 6, 14);
        Shape3.setRotationPoint(7F, 18F, -7F);
        Shape3.setTextureSize(128, 64);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 0, 25);
        Shape4.addBox(0F, 0F, 0F, 1, 6, 14);
        Shape4.setRotationPoint(-8F, 18F, -7F);
        Shape4.setTextureSize(128, 64);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 71, 0);
        Shape5.addBox(0F, 0F, 0F, 14, 1, 14);
        Shape5.setRotationPoint(-7F, 18.5F, -7F); // 18.5 - 19
        Shape5.setTextureSize(128, 64);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 70, 16);
        Shape6.addBox(0F, 0F, 0F, 14, 1, 14);
        Shape6.setRotationPoint(-7F, 18.5F, -7F);
        Shape6.setTextureSize(128, 64);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 33, 35);
        Shape7.addBox(0F, 0F, 0F, 14, 1, 14);
        Shape7.setRotationPoint(-7F, 23F, -7F);
        Shape7.setTextureSize(128, 64);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5, false, false);
    }

    public void render(float f5, boolean planted, boolean compostPresent) {
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);

        if (!planted) {
            GL11.glTranslatef(0, 1 / 16f, 0);
        }
        if (compostPresent) {
            Shape5.render(f5);
        } else {
            Shape6.render(f5);
        }
        if (!planted) {
            GL11.glTranslatef(0, -1 / 16f, 0);
        }

        Shape7.render(f5);
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
