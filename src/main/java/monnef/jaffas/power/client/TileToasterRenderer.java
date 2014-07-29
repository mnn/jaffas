/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.jaffas.food.client.TileSpecialJaffaRenderer;
import monnef.jaffas.power.block.TileToaster;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileToasterRenderer extends TileSpecialJaffaRenderer {
    private ModelToaster toaster;

    public TileToasterRenderer() {
        toaster = new ModelToaster();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileToaster) tile, par2, par4, par6, par8);
    }

    @Override
    protected String[] getTexturePaths() {
        return new String[]{"jaffas_toaster.png"};
    }

    public void renderModelAt(TileToaster tile, double x, double y, double z, float par8) {
        int rotation = tile.getRotation().ordinal();
        rotation = (rotation + 1) % 4;

        // fix for inventory rendering
        if (tile.getWorldObj() == null) rotation = 0;

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
        bindTexture(textures[0]);
        GL11.glRotatef(angle, 0, 1.0f, 0);

        toaster.render(0.0625F);

        if (tile.getWorldObj() != null) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        PowerLabels.renderLabel(tile, x, y, z, false);
    }

}
