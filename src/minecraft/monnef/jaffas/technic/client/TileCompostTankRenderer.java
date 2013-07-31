/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.jaffas.technic.block.TileCompostCore;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileCompostTankRenderer extends TileEntitySpecialRenderer {
    private ModelCompostTank compost;

    public TileCompostTankRenderer() {
        compost = new ModelCompostTank();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileCompostCore) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileCompostCore tile, double par2, double par4, double par6, float par8) {
        if (!tile.getIsValidMultiblock()) return;

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F - 1, 0.5F - 1F, 0.5F - 1);
        bindTextureByName("/jaffas_composttank.png");

        compost.render(0.0625F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
