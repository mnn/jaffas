/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.jaffas.technic.block.TileHighPlant;
import net.minecraft.util.ResourceLocation;

public interface IHighPlantModel {
    void render(TileHighPlant tile, float scale);

    ResourceLocation getTextureFile();
}
