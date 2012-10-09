package jaffas.food;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemJaffaBase extends Item {
	public ItemJaffaBase(int v) {
		super(v);
		maxStackSize = 64;
		this.setTabToDisplayOn(CreativeTabs.tabMaterials);
	}
	
	public String getTextureFile(){
		return "/jaffas_01.png";
	}
}
