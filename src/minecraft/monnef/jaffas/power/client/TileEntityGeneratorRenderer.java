package monnef.jaffas.power.client;

import monnef.jaffas.power.block.BlockGenerator;
import monnef.jaffas.power.block.TileEntityGenerator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityGeneratorRenderer extends TileEntitySpecialRenderer {
    private ModelGenerator generator;

    public TileEntityGeneratorRenderer() {
        generator = new ModelGenerator();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileEntityGenerator) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileEntityGenerator tile, double x, double y, double z, float par8) {

        int meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

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

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        bindTextureByName("/jaffas_generator.png");
        GL11.glRotatef(angle, 0, 1.0f, 0);

        generator.render(0.0625F, BlockGenerator.isBurning(meta));

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        PowerLabels.render(tile, x, y, z, false);
    }

}
