/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

import monnef.jaffas.food.client.TileSpecialJaffaRenderer;
import monnef.jaffas.xmas.client.ModelCandy;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileCandyRenderer extends TileSpecialJaffaRenderer {
    private final ResourceLocation texture;
    private ModelCandy candy;

    public TileCandyRenderer() {
        candy = new ModelCandy();
        texture = new ResourceLocation("/jaffas_candy.png");
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileCandy) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileCandy tile, double par2, double par4, double par6, float par8) {

        int meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
        if (BlockCandy.isBlockTopPart(meta)) return;

        float angle;
        switch (meta) {
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
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        bindTexture(texture);
        GL11.glRotatef(angle, 0, 1.0f, 0);

        candy.render(0.0625F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}