/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

import monnef.jaffas.xmas.client.ModelPresent;
import monnef.jaffas.xmas.client.ModelPresentSmall;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TilePresentRenderer extends TileEntitySpecialRenderer {
    private ModelPresent present;
    private ModelPresentSmall presentSmall;

    private static final ResourceLocation[] TEXTURES;
    public static final String TEXTURE_NAME_STRING = "/jaffas_present_%d.png";

    static {
        TEXTURES = new ResourceLocation[6];
        for (int i = 0; i <= 5; i++) {
            TEXTURES[i] = new ResourceLocation(String.format(TEXTURE_NAME_STRING, i));
        }
    }

    public TilePresentRenderer() {
        present = new ModelPresent();
        presentSmall = new ModelPresentSmall();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TilePresent) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TilePresent tile, double par2, double par4, double par6, float par8) {

        int meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

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
        bindTexture(TEXTURES[meta % 6]);
    }
}
