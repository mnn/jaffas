/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client.fungi;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public abstract class ModelFungi extends ModelBase {
    private final ResourceLocation fungiTexture;

    protected ModelFungi() {
        fungiTexture = new ResourceLocation(getTexture());
    }

    public abstract void render(float f5);

    public abstract String getTexture();

    public ResourceLocation getTextureResource() {
        return fungiTexture;
    }
}
