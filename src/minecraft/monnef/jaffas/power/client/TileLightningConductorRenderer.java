/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.jaffas.power.block.TileLightningConductor;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileLightningConductorRenderer extends TileEntitySpecialRenderer {
    private final ResourceLocation texture;
    private ModelLightningConductor conductor;

    public TileLightningConductorRenderer() {
        conductor = new ModelLightningConductor();
        texture = new ResourceLocation("/jaffas_conductor.png");
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileLightningConductor) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileLightningConductor tile, double x, double y, double z, float par8) {
        GL11.glPushMatrix();

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);

        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);

        bindTexture(texture);

        conductor.render(0.0625F);

        if (tile.worldObj != null) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setTileEntityRenderer(TileEntityRenderer par1TileEntityRenderer) {
        super.setTileEntityRenderer(par1TileEntityRenderer);
    }
}
