/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

// Date: 18.1.2013 21:18:09
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGenerator extends ModelBase {
    //fields
    ModelRenderer Shape33;
    ModelRenderer Shape1;
    ModelRenderer Shape22;
    ModelRenderer Shape25;
    ModelRenderer Shape27;
    ModelRenderer Shape28;
    ModelRenderer Shape354;
    ModelRenderer Shape378;
    ModelRenderer Shape3782;
    ModelRenderer Shape3781;
    ModelRenderer burned;
    ModelRenderer burning;
    ModelRenderer Shape29999;
    ModelRenderer Shape21548;
    ModelRenderer Shape274478;
    ModelRenderer Shape211155;

    public ModelGenerator()
    {
        textureWidth = 256;
        textureHeight = 128;

        Shape33 = new ModelRenderer(this, 92, 28);
        Shape33.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape33.setRotationPoint(-8F, 22F, -8F);
        Shape33.setTextureSize(256, 128);
        Shape33.mirror = true;
        setRotation(Shape33, 0F, 0F, 0F);
        Shape1 = new ModelRenderer(this, 1, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape1.setRotationPoint(-8F, 8F, -8F);
        Shape1.setTextureSize(256, 128);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape22 = new ModelRenderer(this, 132, 0);
        Shape22.addBox(0F, 0F, 0F, 2, 13, 2);
        Shape22.setRotationPoint(6F, 9F, -8F);
        Shape22.setTextureSize(256, 128);
        Shape22.mirror = true;
        setRotation(Shape22, 0F, 0F, 0F);
        Shape25 = new ModelRenderer(this, 150, 0);
        Shape25.addBox(0F, 0F, 0F, 2, 13, 2);
        Shape25.setRotationPoint(6F, 9F, 6F);
        Shape25.setTextureSize(256, 128);
        Shape25.mirror = true;
        setRotation(Shape25, 0F, 0F, 0F);
        Shape27 = new ModelRenderer(this, 160, 0);
        Shape27.addBox(0F, 0F, 0F, 2, 13, 2);
        Shape27.setRotationPoint(-8F, 9F, -8F);
        Shape27.setTextureSize(256, 128);
        Shape27.mirror = true;
        setRotation(Shape27, 0F, 0F, 0F);
        Shape28 = new ModelRenderer(this, 173, 0);
        Shape28.addBox(0F, 0F, 0F, 2, 13, 2);
        Shape28.setRotationPoint(-8F, 9F, 6F);
        Shape28.setTextureSize(256, 128);
        Shape28.mirror = true;
        setRotation(Shape28, 0F, 0F, 0F);
        Shape354 = new ModelRenderer(this, 67, 12);
        Shape354.addBox(0F, 0F, 0F, 12, 8, 1);
        Shape354.setRotationPoint(-6F, 14F, -8F);
        Shape354.setTextureSize(256, 128);
        Shape354.mirror = true;
        setRotation(Shape354, 0F, 0F, 0F);
        Shape378 = new ModelRenderer(this, 68, 0);
        Shape378.addBox(0F, 0F, 0F, 12, 8, 1);
        Shape378.setRotationPoint(-6F, 14F, 7F);
        Shape378.setTextureSize(256, 128);
        Shape378.mirror = true;
        setRotation(Shape378, 0F, 0F, 0F);
        Shape3782 = new ModelRenderer(this, 100, 0);
        Shape3782.addBox(0F, 0F, 0F, 1, 8, 12);
        Shape3782.setRotationPoint(-8F, 14F, -6F);
        Shape3782.setTextureSize(256, 128);
        Shape3782.mirror = true;
        setRotation(Shape3782, 0F, 0F, 0F);
        Shape3781 = new ModelRenderer(this, 63, 27);
        Shape3781.addBox(0F, 0F, 0F, 1, 8, 12);
        Shape3781.setRotationPoint(7F, 14F, -6F);
        Shape3781.setTextureSize(256, 128);
        Shape3781.mirror = true;
        setRotation(Shape3781, 0F, 0F, 0F);
        burned = new ModelRenderer(this, 0, 24);
        burned.addBox(0F, 0F, 0F, 14, 7, 14);
        burned.setRotationPoint(-7F, 15F, -7F);
        burned.setTextureSize(256, 128);
        burned.mirror = true;
        setRotation(burned, 0F, 0F, 0F);
        burning = new ModelRenderer(this, 9, 53);
        burning.addBox(0F, 0F, 0F, 14, 1, 14);
        burning.setRotationPoint(-7F, 15F, -7F);
        burning.setTextureSize(256, 128);
        burning.mirror = true;
        setRotation(burning, 0F, 0F, 0F);
        Shape29999 = new ModelRenderer(this, 0, 58);
        Shape29999.addBox(0F, 0F, 0F, 2, 1, 2);
        Shape29999.setRotationPoint(-8F, 23F, 6F);
        Shape29999.setTextureSize(256, 128);
        Shape29999.mirror = true;
        setRotation(Shape29999, 0F, 0F, 0F);
        Shape21548 = new ModelRenderer(this, 0, 63);
        Shape21548.addBox(0F, 0F, 0F, 2, 1, 2);
        Shape21548.setRotationPoint(6F, 23F, 6F);
        Shape21548.setTextureSize(256, 128);
        Shape21548.mirror = true;
        setRotation(Shape21548, 0F, 0F, 0F);
        Shape274478 = new ModelRenderer(this, 0, 53);
        Shape274478.addBox(0F, 0F, 0F, 2, 1, 2);
        Shape274478.setRotationPoint(6F, 23F, -8F);
        Shape274478.setTextureSize(256, 128);
        Shape274478.mirror = true;
        setRotation(Shape274478, 0F, 0F, 0F);
        Shape211155 = new ModelRenderer(this, 0, 68);
        Shape211155.addBox(0F, 0F, 0F, 2, 1, 2);
        Shape211155.setRotationPoint(-8F, 23F, -8F);
        Shape211155.setTextureSize(256, 128);
        Shape211155.mirror = true;
        setRotation(Shape211155, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5, false);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void render(float f5, boolean isBurning) {
        Shape33.render(f5);
        Shape1.render(f5);
        Shape22.render(f5);
        Shape25.render(f5);
        Shape27.render(f5);
        Shape28.render(f5);
        Shape354.render(f5);
        Shape378.render(f5);
        Shape3782.render(f5);
        Shape3781.render(f5);
        Shape29999.render(f5);
        Shape21548.render(f5);
        Shape274478.render(f5);
        Shape211155.render(f5);

        if (isBurning) {
            burning.render(f5);
        } else {
            burned.render(f5);
        }
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
