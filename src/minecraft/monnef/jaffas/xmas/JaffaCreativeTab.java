package monnef.jaffas.xmas;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    public JaffaCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(jaffasXmas.ItemGiantCandy);
    }
}
