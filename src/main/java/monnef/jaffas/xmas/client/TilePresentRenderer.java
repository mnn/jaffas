/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.client;

import monnef.jaffas.food.client.TileSpecialJaffaRenderer;
import monnef.jaffas.xmas.block.TilePresent;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TilePresentRenderer extends TileSpecialJaffaRenderer {
    public static final int PRESENTS_COUNT = 6;
    private ModelPresent present;
    private ModelPresentSmall presentSmall;

    public static final String TEXTURE_NAME_STRING = "jaffas_present_%d.png";

    public TilePresentRenderer() {
        present = new ModelPresent();
        presentSmall = new ModelPresentSmall();
    }

    @Override
    protected String[] getTexturePaths() {
        String[] res = new String[PRESENTS_COUNT];
        for (int i = 0; i < PRESENTS_COUNT; i++) {
            res[i] = String.format(TEXTURE_NAME_STRING, i);
        }
        return res;
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TilePresent) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TilePresent tile, double par2, double par4, double par6, float par8) {

        int meta = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        //        GL11.glRotatef(angle, 0, 1.0f, 0);

        bindPresentTexture(meta);

        if (meta < 6) {
            present.render(0.0625F);
        } else {
            GL11.glScalef(.7f, .7f, .7f);
            presentSmall.render(0.0625F);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void bindPresentTexture(int meta) {
        bindTexture(textures[meta % 6]);
    }
}
