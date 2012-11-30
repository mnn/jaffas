package monnef.jaffas.food;

import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemJaffaPlate extends ItemArmor implements IArmorTextureProvider{

    public ItemJaffaPlate(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
        super(par1, par2EnumArmorMaterial, par3, par4);
        this.setCreativeTab(mod_jaffas.CreativeTab);
    }

    public String getTextureFile(){
        return "/jaffas_01.png";

    }

    public String getArmorTextureFile(ItemStack par1){
            return "/jaffabrn1.png";
    }

}
