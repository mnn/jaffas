/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMeat extends ModelBase {
    //fields
    ModelRenderer Shape14;
    ModelRenderer Shape15;
    ModelRenderer Shape16;

    public ModelMeat() {
        textureWidth = 128;
        textureHeight = 128;

        Shape14 = new ModelRenderer(this, 10, 15);
        Shape14.addBox(0F, 0F, 0F, 1, 5, 4);
        Shape14.setRotationPoint(-1F, 19F, -1F);
        Shape14.setTextureSize(128, 128);
        Shape14.mirror = true;
        setRotation(Shape14, 0F, 0F, 0F);
        Shape15 = new ModelRenderer(this, 12, 6);
        Shape15.addBox(0F, 0F, 0F, 1, 3, 3);
        Shape15.setRotationPoint(-1F, 16F, -1F);
        Shape15.setTextureSize(128, 128);
        Shape15.mirror = true;
        setRotation(Shape15, 0F, 0F, 0F);
        Shape16 = new ModelRenderer(this, 0, 9);
        Shape16.addBox(0F, 0F, 0F, 1, 1, 0);
        Shape16.setRotationPoint(-1F, 15F, 0.5F);
        Shape16.setTextureSize(128, 128);
        Shape16.mirror = true;
        setRotation(Shape16, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5);
    }

    public void render(float f5) {
        Shape14.render(f5);
        Shape15.render(f5);
        Shape16.render(f5);
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
