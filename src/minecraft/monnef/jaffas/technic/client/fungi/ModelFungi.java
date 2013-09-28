/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client.fungi;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

import static monnef.core.client.PackageToModIdRegistry.searchModIdFromCurrentPackage;
import static monnef.core.client.ResourcePathHelper.ResourceTextureType.TILE;
import static monnef.core.client.ResourcePathHelper.assembleAndCreate;

public abstract class ModelFungi extends ModelBase {
    private final ResourceLocation fungiTexture;

    protected ModelFungi() {
        fungiTexture = assembleAndCreate(getTexture(), searchModIdFromCurrentPackage(), TILE);
    }

    public abstract void render(float f5);

    public abstract String getTexture();

    public ResourceLocation getTextureResource() {
        return fungiTexture;
    }
}
