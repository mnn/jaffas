/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.jaffas.technic.block.TileHighPlant;
import monnef.jaffas.technic.common.HighPlantCatalog;
import monnef.jaffas.technic.common.HighPlantInfo;
import monnef.jaffas.technic.common.IHighPlantModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Collection;
import java.util.HashMap;

public class TileHighPlantRenderer extends TileEntitySpecialRenderer {
    public static final float U = 0.0625F;
    private ModelStake stake;
    private static HashMap<Integer, IHighPlantModel> plantModel;

    static {
        plantModel = new HashMap<Integer, IHighPlantModel>();

        Collection<HighPlantInfo> tmp = HighPlantCatalog.getPlants();
        for (HighPlantInfo plant : tmp) {
            plantModel.put(plant.id, plant.createRenderer());
        }
    }

    public TileHighPlantRenderer() {
        stake = new ModelStake();
    }

    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8) {
        renderModelAt((TileHighPlant) tile, par2, par4, par6, par8);
    }

    public void renderModelAt(TileHighPlant tile, double par2, double par4, double par6, float par8) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);

        int meta = tile.getBlockMetadata();
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
        GL11.glRotatef(angle, 0, 1.0f, 0);
        bindTextureByName("/jaffas_hop_plant.png");
        stake.render(U);
        GL11.glPopMatrix();

        int rot = tile.getRenderRotation();
        GL11.glRotatef(rot * 90, 0, 1.0f, 0);
        HighPlantInfo info = tile.getPlantInfo();
        if (info != null) {
            IHighPlantModel renderer = plantModel.get(info.id);
            bindTextureByName(renderer.getTextureFile());
            renderer.render(tile, U);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
