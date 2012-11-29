package monnef.jaffas.xmas;

import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityCandyRenderer extends TileEntitySpecialRenderer {
    private ModelCandy candy;

    public TileEntityCandyRenderer() {
        candy = new ModelCandy();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileEntityCandy) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileEntityCandy tile, double par2, double par4, double par6, float par8) {

        int meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        bindTextureByName("/jaffas_candy.png");

        candy.render(0.0625F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}