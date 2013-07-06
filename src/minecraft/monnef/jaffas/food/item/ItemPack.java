/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class ItemPack extends ItemJaffaBase {
    public static final String CONTENT_ID_TAG = "contentId";
    public static final String CONTENT_SIZE_TAG = "contentSize";
    public static final String CONTENT_META_TAG = "contentMeta";

    public ItemPack(int id) {
        super(id);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!properNBT(stack)) return stack;
        PlayerHelper.giveItemToPlayer(player, getContent(stack));

        stack.stackSize--;
        return stack;
    }

    protected boolean properNBT(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return false;
        if (!tag.hasKey(CONTENT_ID_TAG)) return false;
        if (!tag.hasKey(CONTENT_SIZE_TAG)) return false;
        if (!tag.hasKey(CONTENT_META_TAG)) return false;

        return true;
    }

    protected ItemStack getContent(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return new ItemStack(tag.getInteger(CONTENT_ID_TAG), tag.getInteger(CONTENT_SIZE_TAG), tag.getInteger(CONTENT_META_TAG));
    }

    public void setContent(ItemStack stack, ItemStack content) {
        setContent(stack, content.itemID, content.stackSize, content.getItemDamage());
    }

    public void setContent(ItemStack stack, int contentId, int contentSize, int contentMeta) {
        initNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger(CONTENT_ID_TAG, contentId);
        tag.setInteger(CONTENT_SIZE_TAG, contentSize);
        tag.setInteger(CONTENT_META_TAG, contentMeta);
    }
}
