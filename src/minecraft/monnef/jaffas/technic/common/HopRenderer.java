/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.jaffas.technic.block.TileEntityHighPlant;
import monnef.jaffas.technic.client.ModelHopPlant;

public class HopRenderer implements IHighPlantModel {
    ModelHopPlant hop;

    public HopRenderer() {
        hop = new ModelHopPlant();
    }

    @Override
    public void render(TileEntityHighPlant tile, float scale) {
        hop.render(scale, tile.getStage());
    }

    @Override
    public String getTextureFile() {
        return "/jaffas_hop_plant.png";
    }
}
