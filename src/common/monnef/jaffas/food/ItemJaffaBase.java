package monnef.jaffas.food;

import net.minecraft.src.Item;

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
