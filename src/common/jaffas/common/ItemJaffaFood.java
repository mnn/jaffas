package jaffas.common;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemFood;

public class ItemJaffaFood extends ItemFood {

	public ItemJaffaFood(int id, int healAmount, float saturation) {
		super(id, healAmount, saturation, false);
		maxStackSize = 64;
		this.setTabToDisplayOn(CreativeTabs.tabFood);
	}
	
	public String getTextureFile(){
		return "/jaffas_01.png";
	}	
}
