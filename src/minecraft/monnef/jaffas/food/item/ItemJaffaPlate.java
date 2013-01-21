package monnef.jaffas.food.item;

import monnef.jaffas.food.mod_jaffas;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemJaffaPlate extends ItemArmor implements IArmorTextureProvider {

    private String armorTexture;

    // helm 0, chest 1, legg 2, boots 3

    public enum ArmorType {
        helm, chest, leggings, boots
    }

    public ItemJaffaPlate(int par1, EnumArmorMaterial par2EnumArmorMaterial, int renderIndex, ArmorType type, String armorTexture) {
        super(par1, par2EnumArmorMaterial, renderIndex, type.ordinal());
        this.armorTexture = armorTexture;
        this.setCreativeTab(mod_jaffas.CreativeTab);
        setItemName("armor." + par2EnumArmorMaterial.name());
    }

    public String getTextureFile() {
        return "/jaffas_01.png";

    }

    public String getArmorTextureFile(ItemStack par1) {
        return armorTexture;
    }

}
