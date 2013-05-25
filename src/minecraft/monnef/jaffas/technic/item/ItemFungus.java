/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.jaffas.technic.fungi.FungiCatalog;
import monnef.jaffas.technic.fungi.FungusInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;
import java.util.Map;

public class ItemFungus extends ItemTechnic {
    public ItemFungus(int id, int textureIndex) {
        super(id, textureIndex);
        setIconsCount(FungiCatalog.getMaxId() + 1); // maximal id to count
        setHasSubtypes(true);
    }

    @Override
    public Icon getIconFromDamage(int meta) {
        return icons[meta];
    }

    @Override
    public void getSubItems(int par1, CreativeTabs tabs, List list) {
        for (Map.Entry<Integer, FungusInfo> fungus : FungiCatalog.catalog.entrySet()) {
            if (fungus.getValue().ordinalItemBind) {
                list.add(new ItemStack(this, 1, fungus.getKey()));
            }
        }
    }
}
