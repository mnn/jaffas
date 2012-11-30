package monnef.jaffas.ores;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;

public class JaffaCreativeTab extends CreativeTabs {
    public JaffaCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(mod_jaffas_ores.JaffarrolRefined);
    }
}
