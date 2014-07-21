/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.jaffas.food.client.TileSpecialJaffaRenderer;
import monnef.jaffas.power.block.TileLightningConductor;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileLightningConductorRenderer extends TileSpecialJaffaRenderer {
    private ModelLightningConductor conductor;

    public TileLightningConductorRenderer() {
        conductor = new ModelLightningConductor();
    }

    @Override
    protected String[] getTexturePaths() {
        return new String[]{"jaffas_conductor.png"};
    }

    @Override
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

        bindTexture(textures[0]);

        conductor.render(0.0625F);

        if (tile.hasWorldObj()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void func_147497_a(TileEntityRendererDispatcher par1TileEntityRenderer) {
        super.func_147497_a(par1TileEntityRenderer);
    }
}
