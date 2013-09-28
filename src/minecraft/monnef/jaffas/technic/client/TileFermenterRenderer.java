/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.jaffas.food.client.TileSpecialJaffaRenderer;
import monnef.jaffas.technic.block.TileFermenter;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileFermenterRenderer extends TileSpecialJaffaRenderer {
    public static final float U = 0.0625F;
    private ModelFermenter fermenter;

    public TileFermenterRenderer() {
        fermenter = new ModelFermenter();
    }

    @Override
    protected String[] getTexturePaths() {
        return new String[]{"jaffas_fermenter.png"};
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileFermenter) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileFermenter tile, double par2, double par4, double par6, float par8) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);

        int meta = tile.getBlockMetadata();
        float angle;
        switch (meta & 3) {
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

        GL11.glRotatef(angle, 0, 1.0f, 0);
        bindTexture(textures[0]);
        fermenter.render(U);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
