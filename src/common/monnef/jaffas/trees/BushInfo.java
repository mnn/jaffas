package monnef.jaffas.trees;

import net.minecraft.src.Item;

public class BushInfo {
    public int itemSeedsID;
    public ItemJaffaSeeds itemSeeds;

    public int blockID;
    public BlockJaffaCrops block;

    public int itemFruitID;
    public ItemJaffaBerry itemFruit;

    public String name;
    public String seedsName;
    public int seedsTexture;
    public String plantName;
    public int plantTexture;
    public String fruitName;
    public int fruitTexture;
    public Item product;
    public int phases;
    public int renderer;
    public mod_jaffas_trees.bushType type;

    public String getSeedsConfigName() {
        return this.name + " seeds";
    }

    public String getBlockConfigName() {
        return this.name + " block";
    }

    public String getFruitConfigName() {
        return this.name + " fruit";
    }

    public String getSeedsLanguageName() {
        return this.name + ".seeds";
    }

    public String getPlantLanguageName() {
        return this.name + ".plant";
    }

    public String getFruitLanguageName() {
        return this.name + ".fruit";
    }
}
