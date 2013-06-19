/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelStake extends ModelBase {
    //fields
    ModelRenderer Shape2;
    ModelRenderer Shape3;

    public ModelStake() {
        textureWidth = 128;
        textureHeight = 128;

        Shape2 = new ModelRenderer(this, 0, 3);
        Shape2.addBox(0F, 0F, 0F, 2, 28, 1);
        Shape2.setRotationPoint(-1F, -4F, 0F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 0);
        Shape3.addBox(0F, 0F, 0F, 16, 1, 1);
        Shape3.setRotationPoint(-8F, 3F, 0F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5);
    }

    public void render(float f5) {
        Shape2.render(f5);
        Shape3.render(f5);
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
