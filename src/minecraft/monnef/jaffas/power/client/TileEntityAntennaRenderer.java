package monnef.jaffas.power.client;

import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
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

    public void renderModelAt(TileEntityAntenna tile, double x, double y, double z, float par8) {

        int meta = tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

        float angle[] = new float[3];
        angle[0] = angle[1] = angle[2] = 0;
        float shift[] = new float[3];
        shift[0] = shift[1] = shift[2] = 0;
        ForgeDirection rotation = ForgeDirection.VALID_DIRECTIONS[mod_jaffas_power.antenna.getRotation(meta)];
        switch (rotation) {
            case UP:
                angle[0] = 0;
                angle[2] = 0;
                break;

            case DOWN:
                angle[0] = 0;
                angle[2] = 180;

                shift[0] = 0;
                shift[1] = 2;
                break;

            case WEST:
                angle[0] = 90;
                angle[2] = 270;

                shift[0] = -1;
                shift[1] = 1;
                break;

            case EAST:
                angle[0] = 90;
                angle[2] = 90;

                shift[0] = 1;
                shift[1] = 1;
                shift[2] = 0;
                break;

            case SOUTH:
                angle[0] = 90;
                angle[2] = 0;

                shift[0] = 0;
                shift[1] = 1;
                shift[2] = -1;
                break;

            case NORTH:
                angle[0] = 90;
                angle[2] = 180;

                shift[0] = 0;
                shift[1] = 1;
                shift[2] = 1;
                break;

            default:
                angle[0] = 30;
                angle[2] = 60;
                break;
        }

        GL11.glPushMatrix();

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);

        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        bindTextureByName("/jaffas_antenna.png");

        GL11.glTranslatef(shift[0], shift[1], shift[2]);

        GL11.glRotatef(angle[0], 1.0f, 0, 0);
        GL11.glRotatef(angle[1], 0, 1.0f, 0);
        GL11.glRotatef(angle[2], 0, 0, 1.0f);

        antenna.render(0.0625F, mod_jaffas_power.antenna.isLit(meta));

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        PowerLabels.renderLabel(tile, x, y, z, false);
    }

}
