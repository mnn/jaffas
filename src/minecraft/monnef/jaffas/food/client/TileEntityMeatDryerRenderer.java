/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.jaffas.food.block.TileEntityMeatDryer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static monnef.jaffas.food.block.TileEntityMeatDryer.MeatState;

public class TileEntityMeatDryerRenderer extends TileEntitySpecialRenderer {
    public static final float U = 0.0625F;
    private ModelMeatRack rack;
    private ModelMeat meat;
    public static final float X_SHIFT = -11 * U;
    public static final float Y_SHIFT = 6 * U;
    private static final float[][] meatShifts = new float[][]{
            new float[]{0, 0},
            new float[]{X_SHIFT, 0},
            new float[]{0, Y_SHIFT},
            new float[]{X_SHIFT, Y_SHIFT},
    };

    public TileEntityMeatDryerRenderer() {
        rack = new ModelMeatRack();
        meat = new ModelMeat();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileEntityMeatDryer) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileEntityMeatDryer tile, double par2, double par4, double par6, float par8) {

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
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        GL11.glRotatef(angle, 0, 1.0f, 0);

        bindTextureByName("/jaffas_meat_rack.png");
        rack.render(U);

        GL11.glTranslatef(6 * U, -4 * U, -4 * U);
        for (int i = 0; i < 4; i++) {
            MeatState currentMeat = tile.getCurrentMeatState(i);
            if (currentMeat != MeatState.NO_MEAT) {
                GL11.glPushMatrix();

                float[] shift = meatShifts[i];
                GL11.glTranslatef(shift[0], 0, shift[1]);
                bindMeatTexture(currentMeat);
                meat.render(U);

                GL11.glPopMatrix();
            }
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void bindMeatTexture(MeatState state) {
        String texture;
        switch (state) {
            case ZOMBIE_RAW:
                texture = "/jaffas_meat_zombie_1.png";
                break;

            case ZOMBIE_HALF_DONE:
                texture = "/jaffas_meat_zombie_2.png";
                break;

            case NORMAL_RAW:
                texture = "/jaffas_meat_norm_1.png";
                break;

            case NORMAL_HALF_DONE:
                texture = "/jaffas_meat_norm_2.png";
                break;

            case ZOMBIE_DONE:
            case NORMAL_DONE:
                texture = "/jaffas_meat_final.png";
                break;

            default:
                throw new RuntimeException("unknown meat state - " + state);
        }

        bindTextureByName(texture);
    }

}
