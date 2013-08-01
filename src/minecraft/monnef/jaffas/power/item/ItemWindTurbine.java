/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

public class ItemWindTurbine extends ItemPower {
    private final int model;
    private boolean checkBack;
    private int radius;
    private String modelTexture;
    private boolean usesColoring;

    public ItemWindTurbine(int id, int textureIndex, int durability, int model) {
        super(id, textureIndex);
        this.model = model;
        setMaxDamage(durability);
    }

    public void configure(boolean checkBack, int radius, String texture, boolean usesColoring) {
        this.checkBack = checkBack;
        this.radius = radius;
        this.modelTexture = texture;
        this.usesColoring = usesColoring;
    }

    public int getModel() {
        return model;
    }

    public boolean doesCheckBack() {
        return checkBack;
    }

    public String getModelTexture() {
        return modelTexture;
    }

    public int getRadius() {
        return radius;
    }

    public boolean doesUseColoring() {
        return usesColoring;
    }
}
