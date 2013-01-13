package monnef.jaffas.food.item;


import monnef.jaffas.food.mod_jaffas;
import net.minecraft.item.Item;

public class ItemJaffaBase extends Item {
	public ItemJaffaBase(int v) {
		super(v);
		maxStackSize = 64;
		//this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setCreativeTab(mod_jaffas.CreativeTab);
	}
	
	public String getTextureFile(){
		return "/jaffas_01.png";
	}
}
