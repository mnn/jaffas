/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.client.ResourcePathHelper;
import monnef.jaffas.technic.block.TileHighPlant;
import monnef.jaffas.technic.client.ModelHopPlant;
import net.minecraft.util.ResourceLocation;

import static monnef.core.client.PackageToModIdRegistry.searchModIdFromCurrentPackage;
import static monnef.core.client.ResourcePathHelper.ResourceTextureType.TILE;

public class HopRenderer implements IHighPlantModel {
    public final ResourceLocation texture;
    ModelHopPlant hop;

    public HopRenderer() {
        hop = new ModelHopPlant();
        texture = ResourcePathHelper.assembleAndCreate("jaffas_hop_plant.png", searchModIdFromCurrentPackage(), TILE);
    }

    @Override
    public void render(TileHighPlant tile, float scale) {
        hop.render(scale, tile.getStage());
    }

    @Override
    public ResourceLocation getTextureFile() {
        return texture;
    }
}
