/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.core.utils.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemPack extends ItemJaffaBase {
    public static final String CONTENT_ID_TAG = "contentId";
    public static final String CONTENT_SIZE_TAG = "contentSize";
    public static final String CONTENT_META_TAG = "contentMeta";

    public ItemPack() {
        super();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!properNBT(stack)) return stack;
        PlayerHelper.giveItemToPlayer(player, getContent(stack));

        stack.stackSize--;
        return stack;
    }

    protected static boolean properNBT(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return false;
        if (!tag.hasKey(CONTENT_ID_TAG)) return false;
        if (!tag.hasKey(CONTENT_SIZE_TAG)) return false;
        if (!tag.hasKey(CONTENT_META_TAG)) return false;

        return true;
    }

    protected static ItemStack getContent(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return new ItemStack(Item.getItemById(tag.getInteger(CONTENT_ID_TAG)), tag.getInteger(CONTENT_SIZE_TAG), tag.getInteger(CONTENT_META_TAG));
    }

    public static void setContent(ItemStack stack, ItemStack content) {
        setContent(stack, content.getItem(), content.stackSize, content.getItemDamage());
    }

    public static void setContent(ItemStack stack, Item content, int contentSize, int contentMeta) {
        initNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger(CONTENT_ID_TAG, Item.getIdFromItem(content));
        tag.setInteger(CONTENT_SIZE_TAG, contentSize);
        tag.setInteger(CONTENT_META_TAG, contentMeta);
    }
}
