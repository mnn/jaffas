/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.crafting.Recipes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;

public class ItemJaffaPack extends ItemPack {
    private static LinkedHashMap<JaffaItem, ItemStack> packs;

    public static void initPacks() {
        if (packs != null) {
            throw new RuntimeException("re-initializing!");
        }

        packs = new LinkedHashMap<JaffaItem, ItemStack>();
        for (JaffaItem ji : JaffasHelper.getJaffas()) {
            ItemStack stack = new ItemStack(JaffasFood.getItem(JaffaItem.jaffasPack), 1, 0);
            setContent(stack, JaffasFood.getItem(ji), Recipes.JAFFAS_PACK_CONTENT_SIZE, 0);
            packs.put(ji, stack);
        }
    }

    public static ItemStack getPackOfJaffas(JaffaItem jaffa) {
        ItemStack tmp = packs.get(jaffa);
        return tmp == null ? null : tmp.copy();
    }

    public ItemJaffaPack() {
        super();
    }

    @Override
    public void addInformationCustom(ItemStack stack, EntityPlayer player, List<String> result, boolean par4) {
        super.addInformationCustom(stack, player, result, par4);
        if (properNBT(stack)) {
            int contentId = Item.getIdFromItem(getContent(stack).getItem());
            JaffaItem jaffaItem = JaffasFood.instance.items.getJaffaItem(contentId);
            String title = JaffasHelper.getTitle(jaffaItem);
            result.add("\u00A7f  " + title + "\u00A7r");
        }
    }

    @Override
    public void getSubItems(Item currentItem, CreativeTabs tab, List list) {
        for (JaffaItem jaffaItem : JaffasHelper.getJaffas()) {
            Item packItem = JaffasFood.getItem(jaffaItem);
            ItemStack stack = new ItemStack(currentItem, 1, 0);
            setContent(stack, packItem, Recipes.JAFFAS_PACK_CONTENT_SIZE, 0);
            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }
}
