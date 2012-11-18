package monnef.jaffas.trees;

import net.minecraft.src.*;

public class ItemJaffaSeeds extends ItemSeeds {
    public ItemJaffaSeeds(int id, int blockId, int soilBlockId) {
        super(id, blockId, soilBlockId);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    public String getTextureFile() {
        return "/jaffas_02.png";
    }
}
