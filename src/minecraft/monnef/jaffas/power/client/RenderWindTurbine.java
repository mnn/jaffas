/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.jaffas.power.entity.EntityWindTurbine;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderWindTurbine extends Render {

    private final ModelTest box;

    public RenderWindTurbine() {
        box = new ModelTest();
    }

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        render((EntityWindTurbine) entity, d0, d1, d2, f, f1);
    }

    private void render(EntityWindTurbine turbine, double x, double y, double z, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        box.render(1);
        renderAABB(turbine.getBoundingBox());
        GL11.glPopMatrix();
    }

    private class ModelTest extends ModelBase {
        private final ModelRenderer b;

        private ModelTest() {
            b = new ModelRenderer(this, 0, 0);
            b.addBox(0f, 0f, 0f, 1, 1, 1);
        }

        @Override
        public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
            b.render(par7);
        }

        public void render(float f5) {
            b.render(f5);
        }
    }
}
