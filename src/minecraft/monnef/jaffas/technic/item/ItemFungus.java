/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.jaffas.technic.common.FungiCatalog;
import monnef.jaffas.technic.common.FungusInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        String subTitle = FungiCatalog.get(stack.getItemDamage()).subTitle;
        if (subTitle != null && !subTitle.isEmpty()) {
            list.add(subTitle);
        }
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
