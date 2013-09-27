/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.jaffas.power.block.TileGenerator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileGeneratorRenderer extends TileEntitySpecialRenderer {
    public static final ResourceLocation TEXTURE = new ResourceLocation("/jaffas_generator.png");
    private ModelGenerator generator;

    public TileGeneratorRenderer() {
        generator = new ModelGenerator();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileGenerator) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileGenerator tile, double x, double y, double z, float par8) {
        boolean burning = tile.isBurning();
        int rotation = tile.getRotation().ordinal();

        // fix for inventory rendering
        if (tile.worldObj == null) rotation = 0;

        float angle;
        switch (rotation) {
            case 0:
                angle = 0;
                break;

            case 1:
                angle = 90;
                break;

            case 2:
                angle = 180;
                break;

            case 3:
                angle = -90;
                break;

            default:
                angle = 45;
                break;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        bindTexture(TEXTURE);
        GL11.glRotatef(angle, 0, 1.0f, 0);

        generator.render(0.0625F, burning);

        if (tile.worldObj != null) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        PowerLabels.renderLabel(tile, x, y, z, false);
    }

}
