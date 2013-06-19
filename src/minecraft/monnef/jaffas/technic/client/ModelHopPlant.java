/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHopPlant extends ModelBase {
    //fields
    ModelRenderer b1;
    ModelRenderer b2;
    ModelRenderer c1;
    ModelRenderer c2;
    ModelRenderer c3;
    ModelRenderer c4;
    ModelRenderer c5;
    ModelRenderer d1;
    ModelRenderer d2;
    ModelRenderer d3;
    ModelRenderer d4;
    ModelRenderer d5;
    ModelRenderer d6;
    ModelRenderer a1;

    public ModelHopPlant() {
        textureWidth = 128;
        textureHeight = 128;

        b1 = new ModelRenderer(this, 49, 0);
        b1.addBox(0F, 0F, 0F, 4, 24, 0);
        b1.setRotationPoint(-2F, 0F, 1.1F);
        b1.setTextureSize(128, 128);
        b1.mirror = true;
        setRotation(b1, 0F, 0F, 0F);
        b2 = new ModelRenderer(this, 40, 0);
        b2.addBox(0F, 0F, 0F, 4, 24, 0);
        b2.setRotationPoint(-2F, 0F, -0.1F);
        b2.setTextureSize(128, 128);
        b2.mirror = true;
        setRotation(b2, 0F, 0F, 0F);
        c1 = new ModelRenderer(this, 35, 36);
        c1.addBox(0F, 0F, 0F, 8, 23, 0);
        c1.setRotationPoint(-4F, 0F, -2F);
        c1.setTextureSize(128, 128);
        c1.mirror = true;
        setRotation(c1, 0F, 0F, 0F);
        c2 = new ModelRenderer(this, 54, 36);
        c2.addBox(0F, 0F, 0F, 8, 23, 0);
        c2.setRotationPoint(-4F, 0F, 4F);
        c2.setTextureSize(128, 128);
        c2.mirror = true;
        setRotation(c2, 0F, 0F, 0F);
        c3 = new ModelRenderer(this, 0, 34);
        c3.addBox(0F, 0F, 0F, 0, 24, 8);
        c3.setRotationPoint(3F, 0F, -3F);
        c3.setTextureSize(128, 128);
        c3.mirror = true;
        setRotation(c3, 0F, 0F, 0F);
        c4 = new ModelRenderer(this, 18, 35);
        c4.addBox(0F, 0F, 0F, 0, 24, 8);
        c4.setRotationPoint(-3F, 0F, -3F);
        c4.setTextureSize(128, 128);
        c4.mirror = true;
        setRotation(c4, 0F, 0F, 0F);
        c5 = new ModelRenderer(this, 12, 9);
        c5.addBox(0F, 0F, 0F, 6, 0, 6);
        c5.setRotationPoint(-3F, 0F, -2F);
        c5.setTextureSize(128, 128);
        c5.mirror = true;
        setRotation(c5, 0F, 0F, 0F);
        d1 = new ModelRenderer(this, 11, 17);
        d1.addBox(0F, 0F, 0F, 1, 3, 1);
        d1.setRotationPoint(2F, 3F, 4F);
        d1.setTextureSize(128, 128);
        d1.mirror = true;
        setRotation(d1, 0.1858931F, 0F, 0F);
        d2 = new ModelRenderer(this, 16, 17);
        d2.addBox(0F, 0F, 0F, 1, 3, 1);
        d2.setRotationPoint(0F, 11F, 4F);
        d2.setTextureSize(128, 128);
        d2.mirror = true;
        setRotation(d2, 0.1858931F, 0F, 0F);
        d3 = new ModelRenderer(this, 21, 17);
        d3.addBox(0F, 0F, 0F, 1, 3, 1);
        d3.setRotationPoint(-4F, 6F, 2F);
        d3.setTextureSize(128, 128);
        d3.mirror = true;
        setRotation(d3, 0F, 0F, 0.1745329F);
        d4 = new ModelRenderer(this, 26, 17);
        d4.addBox(0F, 0F, 0F, 1, 3, 1);
        d4.setRotationPoint(4F, 11F, -1F);
        d4.setTextureSize(128, 128);
        d4.mirror = true;
        setRotation(d4, 0F, 0F, -0.1745329F);
        d5 = new ModelRenderer(this, 31, 17);
        d5.addBox(0F, 0F, 0F, 1, 3, 1);
        d5.setRotationPoint(-2F, 10F, -4F);
        d5.setTextureSize(128, 128);
        d5.mirror = true;
        setRotation(d5, -0.1745329F, 0F, 0F);
        d6 = new ModelRenderer(this, 36, 17);
        d6.addBox(0F, 0F, 0F, 1, 3, 1);
        d6.setRotationPoint(3F, 6F, -3F);
        d6.setTextureSize(128, 128);
        d6.mirror = true;
        setRotation(d6, -0.1745329F, 0F, 0F);
        a1 = new ModelRenderer(this, 69, 0);
        a1.addBox(0F, 0F, 0F, 4, 4, 3);
        a1.setRotationPoint(-2F, 20F, -1F);
        a1.setTextureSize(128, 128);
        a1.mirror = true;
        setRotation(a1, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        render(f5, 5);
    }

    private final static Boolean renderMap[][] = {
            {true, false, false, false},
            {false, true, false, false},
            {false, false, true, false},
            {false, false, true, true},
            {true, true, true, true}
    };

    public void render(float f5, int stage) {
        Boolean[] currRenderMap = renderMap[stage];

        if (currRenderMap[0]) {
            a1.render(f5);
        }

        if (currRenderMap[1]) {
            b1.render(f5);
            b2.render(f5);
        }

        if (currRenderMap[2]) {
            c1.render(f5);
            c2.render(f5);
            c3.render(f5);
            c4.render(f5);
            c5.render(f5);
        }

        if (currRenderMap[3]) {
            d1.render(f5);
            d2.render(f5);
            d3.render(f5);
            d4.render(f5);
            d5.render(f5);
            d6.render(f5);
        }
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
