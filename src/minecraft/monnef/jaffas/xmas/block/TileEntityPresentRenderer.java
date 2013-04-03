package monnef.jaffas.xmas.block;

import monnef.jaffas.xmas.client.ModelPresent;
import monnef.jaffas.xmas.client.ModelPresentSmall;
import monnef.jaffas.xmas.jaffasXmas;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityPresentRenderer extends TileEntitySpecialRenderer {
    private ModelPresent present;
    private ModelPresentSmall presentSmall;

    public TileEntityPresentRenderer() {
        present = new ModelPresent();
        presentSmall = new ModelPresentSmall();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileEntityPresent) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileEntityPresent tile, double par2, double par4, double par6, float par8) {

        int meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        //        GL11.glRotatef(angle, 0, 1.0f, 0);

        switch (meta % 6) {
            case 0:
                bindTextureByName("/jaffas_present_0.png");
                break;

            case 1:
                bindTextureByName("/jaffas_present_1.png");
                break;

            case 2:
                bindTextureByName("/jaffas_present_2.png");
                break;

            case 3:
                bindTextureByName("/jaffas_present_3.png");
                break;

            case 4:
                bindTextureByName("/jaffas_present_4.png");
                break;

            case 5:
                bindTextureByName("/jaffas_present_5.png");
                break;

            default:
                bindTextureByName(jaffasXmas.textureFile);
                break;
        }

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
}
