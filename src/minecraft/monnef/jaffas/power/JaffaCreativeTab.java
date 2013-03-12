package monnef.jaffas.power;

import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.technic.mod_jaffas_technic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    public JaffaCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        Item item = ModuleManager.IsModuleEnabled(ModulesEnum.technic) ? mod_jaffas_technic.limsew : Item.redstone;
        return new ItemStack(item);
    }
}
