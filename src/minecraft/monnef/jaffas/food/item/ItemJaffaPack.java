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
            ItemStack stack = new ItemStack(JaffasFood.getItem(JaffaItem.jaffasPack).itemID, 1, 0);
            setContent(stack, JaffasFood.getItem(ji).itemID, Recipes.JAFFAS_PACK_CONTENT_SIZE, 0);
            packs.put(ji, stack);
        }
    }

    public static ItemStack getPackOfJaffas(JaffaItem jaffa) {
        ItemStack tmp = packs.get(jaffa);
        return tmp == null ? null : tmp.copy();
    }

    public ItemJaffaPack(int id) {
        super(id);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (properNBT(stack)) {
            int contentId = getContent(stack).itemID;
            JaffaItem jaffaItem = JaffasFood.instance.items.getJaffaItem(contentId);
            String title = JaffasHelper.getTitle(jaffaItem);
            list.add("§f  " + title + "§r");
        }
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        for (JaffaItem jaffaItem : JaffasHelper.getJaffas()) {
            Item item = JaffasFood.getItem(jaffaItem);
            ItemStack stack = new ItemStack(id, 1, 0);
            setContent(stack, item.itemID, Recipes.JAFFAS_PACK_CONTENT_SIZE, 0);
            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }
}
