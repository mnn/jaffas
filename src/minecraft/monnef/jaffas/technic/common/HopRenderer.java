/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.jaffas.technic.block.TileHighPlant;
import monnef.jaffas.technic.client.ModelHopPlant;
import net.minecraft.util.ResourceLocation;

public class HopRenderer implements IHighPlantModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("/jaffas_hop_plant.png");
    ModelHopPlant hop;

    public HopRenderer() {
        hop = new ModelHopPlant();
    }

    @Override
    public void render(TileHighPlant tile, float scale) {
        hop.render(scale, tile.getStage());
    }

    @Override
    public ResourceLocation getTextureFile() {
        return TEXTURE;
    }
}
