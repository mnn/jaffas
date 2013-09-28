/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.jaffas.food.block.TilePie;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TilePieRenderer extends TileSpecialJaffaRenderer {
    private ModelPie pie;

    private static final String[] textures = new String[]{"/jaffas_pie01.png", "/jaffas_pie02.png", "/jaffas_pie04.png", "/jaffas_pie03.png"};
    private static final ResourceLocation[] textureResources;

    static {
        textureResources = new ResourceLocation[textures.length];
        for (int i = 0; i < textures.length; i++) {
            textureResources[i] = new ResourceLocation(textures[i]);
        }
    }

    public TilePieRenderer() {
        pie = new ModelPie();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TilePie) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TilePie tile, double par2, double par4, double par6, float par8) {
        if (tile == null || tile.type == null) return;

        float angle;
        switch (tile.rotation) {
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
                angle = 270;
                break;
            default:
                angle = 45;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        bindTexture(textureResources[tile.type.ordinal()]);
        GL11.glRotatef(angle, 0, 1.0f, 0);

        pie.render(0.0625F, tile.pieces);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
