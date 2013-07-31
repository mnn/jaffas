/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.jaffas.technic.block.TileHighPlant;

public interface IHighPlantModel {
    void render(TileHighPlant tile, float scale);

    String getTextureFile();
}
