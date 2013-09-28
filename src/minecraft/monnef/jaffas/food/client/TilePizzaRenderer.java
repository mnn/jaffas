/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.jaffas.food.block.TilePizza;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static monnef.jaffas.food.common.ContentHolder.blockPizza;

public class TilePizzaRenderer extends TileSpecialJaffaRenderer {
    private ModelPizza pizza;
    private ResourceLocation texture;

    public TilePizzaRenderer() {
        texture = new ResourceLocation("/jaffas_pizza.png");
        pizza = new ModelPizza();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TilePizza) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TilePizza tile, double par2, double par4, double par6, float par8) {

        int meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

        float angle;
        if (blockPizza.getRotation(meta)) {
            angle = 0;
        } else {
            angle = 90;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        bindTexture(texture);
        GL11.glRotatef(angle, 0, 1.0f, 0);

        pizza.render(0.0625F, blockPizza.getPieces(meta));

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
