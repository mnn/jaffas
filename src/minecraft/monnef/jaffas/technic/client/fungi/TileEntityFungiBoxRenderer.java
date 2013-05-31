/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client.fungi;

import monnef.jaffas.technic.block.TileEntityFungiBox;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Arrays;

public class TileEntityFungiBoxRenderer extends TileEntitySpecialRenderer {
    private ModelFungiBox box = new ModelFungiBox();
    private ModelFungi[][] fungi;

    public TileEntityFungiBoxRenderer() {
        ArrayList<ModelFungi[]> list = new ArrayList<ModelFungi[]>();
        insertModelSeq(list, new ModelFungiOneStageOne(), new ModelFungiOneStageTwo(), new ModelFungiOneStageThree());
        insertModelSeq(list, new ModelFungiParasolStageOne(), new ModelFungiParasolStageTwo(), new ModelFungiParasolStageThree());
        fungi = list.toArray(new ModelFungi[][]{});
    }

    private void insertModelSeq(ArrayList<ModelFungi[]> list, ModelFungi... items) {
        list.add(Arrays.asList(items).toArray(new ModelFungi[]{}));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
        TileEntityFungiBox t = (TileEntityFungiBox) tile;

        GL11.glPushMatrix();

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);

        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);

        bindTextureByName("/jaffas_fungi_box.png");

        GL11.glPushMatrix();
        GL11.glRotatef(t.getRenderRotationBox() * 90, 0, 1, 0);
        box.render(0.0625F, t.mushroomPlanted(), t.compostPresent());
        GL11.glPopMatrix();

        if (t.mushroomPlanted()) {
            int currentFungusModel = t.getModelIndex();
            int renderState = t.getRenderState();

            if (currentFungusModel != -1 && renderState != -1) {
                ModelFungi toRender = fungi[currentFungusModel][renderState];
                bindTextureByName(toRender.getTexture());
                GL11.glRotatef(t.getRenderRotationMushroom() * 90, 0, 1, 0);
                toRender.render(0.0625f);
            }
        }

        if (tile.worldObj != null) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
