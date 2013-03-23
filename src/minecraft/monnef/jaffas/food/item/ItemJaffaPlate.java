package monnef.jaffas.food.item;

import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemJaffaPlate extends ItemArmor implements IArmorTextureProvider {

    private String armorTexture;
    private Item repairItem;

    // helm 0, chest 1, legg 2, boots 3

    public enum ArmorType {
        helm, chest, leggings, boots
    }

    public ItemJaffaPlate(int par1, EnumArmorMaterial par2EnumArmorMaterial, int renderIndex, ArmorType type, String armorTexture, Item repairItem) {
        super(par1, par2EnumArmorMaterial, renderIndex, type.ordinal());
        this.armorTexture = armorTexture;
        this.repairItem = repairItem;
        this.setCreativeTab(mod_jaffas_food.CreativeTab);
        setUnlocalizedName("armor." + par2EnumArmorMaterial.name());
    }

    public String getTextureFile() {
        return mod_jaffas_food.textureFile[0];
    }

    public String getArmorTextureFile(ItemStack par1) {
        return armorTexture;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        if (repairItem == null || repairItem.itemID != par2ItemStack.itemID)
            return super.getIsRepairable(par1ItemStack, par2ItemStack);
        return true;
    }
}
