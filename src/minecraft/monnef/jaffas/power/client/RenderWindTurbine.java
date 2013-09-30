/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.core.client.IModelObj;
import monnef.core.client.ModelObj;
import monnef.core.client.ResourcePathHelper;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.RenderUtils;
import monnef.jaffas.power.entity.EntityWindTurbine;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static monnef.core.client.ResourcePathHelper.ResourceTextureType.ENTITY;

public class RenderWindTurbine extends Render {
    private static ArrayList<TurbineRenderer> models = new ArrayList<TurbineRenderer>();

    static {
        models.add(new TurbineRenderer("/jaffas_windmill01_hub.obj", "jaffas_windmill01_hub.png", "/jaffas_windmill01_blades.obj", "jaffas_windmill01_blades.png", 90, 0.15f));
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
        ForgeDirection turbineRotation = turbine.getTurbineRotation();
        RenderUtils.rotate(turbineRotation);
        float turbineSpin = turbine.animationRotation + partialTickTime * turbine.getCurrentRotationPerTick();
        turbineSpin *= isRotationOdd(turbineRotation) ? -1 : 1;
        models.get(turbine.getModelId()).render(1, turbine.getColor(), turbineSpin);
        GL11.glPopMatrix();
    }

    private boolean isRotationOdd(ForgeDirection rotation) {
        return rotation == ForgeDirection.EAST || rotation == ForgeDirection.SOUTH;
    }

    private static class TurbineRenderer {
        private final IModelObj baseModel;
        private final IModelObj coloredModel;
        private final float zOffset;
        private ColorHelper.IntColor tmpColor = new ColorHelper.IntColor();

        public TurbineRenderer(String baseModelFileName, String baseTexture, String coloredModelFileName, String coloredTexture, float rotationFix, float zOffset) {
            this.zOffset = zOffset;
            baseModel = baseModelFileName == null ? null : new ModelObj(baseModelFileName, rotationFix, ResourcePathHelper.assemble(baseTexture, ENTITY));
            coloredModel = coloredModelFileName == null ? null : new ModelObj(coloredModelFileName, rotationFix, ResourcePathHelper.assemble(coloredTexture, ENTITY));
        }

        public void render(float scale, int colour, float turbineSpin) {
            float angle = 360 * turbineSpin;
            GL11.glRotatef(angle, 0, 0, 1);
            GL11.glTranslatef(0, 0, -zOffset);
            if (baseModel != null) {
                baseModel.renderWithTexture(scale);
            }
            if (coloredModel != null) {
                ColorHelper.getColor(colour, tmpColor);
                coloredModel.renderWithTextureAndTint(scale, tmpColor);
            }
            GL11.glTranslatef(0, 0, zOffset);
            GL11.glRotatef(-angle, 0, 0, 1);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
