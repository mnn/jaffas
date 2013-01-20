package monnef.jaffas.power.client;

import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityAntennaRenderer extends TileEntitySpecialRenderer {
    private ModelAntenna antenna;

    public TileEntityAntennaRenderer() {
        antenna = new ModelAntenna();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileEntityAntenna) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileEntityAntenna tile, double par2, double par4, double par6, float par8) {

        int meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

        // TODO angles
        float angle;
        switch (mod_jaffas_power.antenna.getRotation(meta)) {
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
        bindTextureByName("/jaffas_antenna.png");
        GL11.glRotatef(angle, 0, 1.0f, 0);

        antenna.render(0.0625F, mod_jaffas_power.antenna.isLit(meta));

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
