package monnef.jaffas.xmas.item;

import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.common.Reference;
import net.minecraft.item.Item;

public class ItemXmas extends ItemJaffaBase {
    public ItemXmas(int id) {
        super(id);
        init();
    }

    public ItemXmas(int id, int textureIndex) {
        super(id);
        setCustomIconIndex(textureIndex);
        init();
    }

    private void init() {
        setCreativeTab(JaffasXmas.CreativeTab);
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        return super.setUnlocalizedName("jaffas.technic." + par1Str);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 4;
    }
}
