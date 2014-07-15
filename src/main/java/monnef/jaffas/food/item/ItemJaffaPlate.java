/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.core.MonnefCorePlugin;
import monnef.core.client.ResourcePathHelper;
import monnef.core.common.CustomIconHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.List;

import static monnef.core.client.ResourcePathHelper.ResourceTextureType.ARMOR;

public class ItemJaffaPlate extends ItemArmor {
    private String armorTexture;
    private Item repairItem;
    private final int customIconIndex;

    // helm 0, chest 1, legg 2, boots 3

    public enum ArmorType {
        helm, chest, leggings, boots
    }

    public ItemJaffaPlate(ArmorMaterial material, int renderIndex, ArmorType type, String armorTexture, Item repairItem, int customIconIndex) {
        super(material, renderIndex, type.ordinal());
        this.armorTexture = ResourcePathHelper.assemble(armorTexture, ARMOR);
        this.repairItem = repairItem;
        this.customIconIndex = customIconIndex;
        this.setCreativeTab(JaffasFood.instance.creativeTab);
        setUnlocalizedName("armor." + material.name());
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{CreativeTabs.tabCombat, getCreativeTab()};
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return armorTexture;
    }

    @Override
    public boolean getIsRepairable(ItemStack armor, ItemStack material) {
        return repairItem != null && repairItem == material.getItem();
    }

    @Override
    public void registerIcons(IIconRegister register) {
        String id = CustomIconHelper.generateId(Reference.ModName, 1, customIconIndex);
        itemIcon = register.registerIcon(id);
    }

    @Override
    public int getItemEnchantability() {
        return repairItem == null ? -1 : super.getItemEnchantability();
    }

    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        super.getSubItems(item, par2CreativeTabs, par3List);
        if (MonnefCorePlugin.debugEnv) {
            par3List.add(new ItemStack(item, 1, getMaxDamage() - 10));
        }
    }

    public boolean nearlyDestroyed(ItemStack stack) {
        return stack.getItemDamage() > stack.getMaxDamage() - 10;
    }

    public boolean unequipWhenDamaged() {
        return repairItem == null;
    }
}
