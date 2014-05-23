/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.client.PackageToModIdRegistry;
import monnef.core.client.ResourcePathHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

import static monnef.core.client.ResourcePathHelper.ResourceTextureType.TILE;

public abstract class TileSpecialJaffaRenderer extends TileEntitySpecialRenderer {
    private String modId;
    protected ResourceLocation[] textures;

    protected TileSpecialJaffaRenderer() {
        modId = PackageToModIdRegistry.searchModIdFromCurrentPackage(1);
        createTextures();
    }

    protected void createTextures() {
        String[] paths = getTexturePaths();
        textures = new ResourceLocation[paths.length];
        for (int i = 0; i < paths.length; i++) {
            textures[i] = ResourcePathHelper.assembleAndCreate(paths[i], getModId(), TILE);
        }
    }

    protected abstract String[] getTexturePaths();

    public String getModId() {
        return modId;
    }

    protected void setModId(String modId) {
        this.modId = modId;
    }
}
