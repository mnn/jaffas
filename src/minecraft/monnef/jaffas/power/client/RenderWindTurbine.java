/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.core.client.IModelObj;
import monnef.core.client.ModelObj;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.RenderUtils;
import monnef.jaffas.power.entity.EntityWindTurbine;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class RenderWindTurbine extends Render {
    private static ArrayList<TurbineRenderer> models = new ArrayList<TurbineRenderer>();

    static {
        models.add(new TurbineRenderer("/jaffas_windmill01_hub.obj", "/jaffas_logo.png", "/jaffas_windmill01_blades.obj", "/jaffas_testing.png", 90));
    }

    public RenderWindTurbine() {

    }

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float partialTickTime) {
        render((EntityWindTurbine) entity, d0, d1, d2, f, partialTickTime);
    }

    private void render(EntityWindTurbine turbine, double x, double y, double z, float f, float partialTickTime) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        RenderUtils.rotate(turbine.getTurbineRotation());
        float turbineSpin = turbine.animationRotation + partialTickTime * turbine.getItemPrototype().getRotationSpeedPerTick();
        models.get(turbine.getModelId()).render(1, turbine.getColor(), turbineSpin);
        GL11.glPopMatrix();
    }

    private static class TurbineRenderer {
        private final IModelObj baseModel;
        private final IModelObj coloredModel;
        private ColorHelper.IntColor tmpColor = new ColorHelper.IntColor();

        public TurbineRenderer(String baseModelFileName, String baseTexture, String coloredModelFileName, String coloredTexture, float rotationFix) {
            baseModel = baseModelFileName == null ? null : new ModelObj(baseModelFileName, rotationFix, baseTexture);
            coloredModel = coloredModelFileName == null ? null : new ModelObj(coloredModelFileName, rotationFix, coloredTexture);
        }

        public void render(float scale, int colour, float turbineSpin) {
            GL11.glRotatef(360, 0, 0, turbineSpin);
            if (baseModel != null) {
                baseModel.renderWithTexture(scale);
            }
            if (coloredModel != null) {
                ColorHelper.getColor(colour, tmpColor);
                GL11.glColor4f(tmpColor.getRed(), tmpColor.getGreen(), tmpColor.getBlue(), 1);
                coloredModel.renderWithTexture(scale);
            }
            GL11.glRotatef(-360, 0, 0, turbineSpin);
        }
    }
}
