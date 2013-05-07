/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.item;

import monnef.core.ItemStackInInventory;
import monnef.core.utils.InventoryUtils;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.common.SeedsHelper;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMagnifier extends ItemJaffaRecipeTool {
    private static final int MAXIMAL_IDS_PER_RUN = 32;
    private ItemStack needle;

    public ItemMagnifier(int id) {
        super(id, 128);
        setInfo("Any unknown seeds?");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) return stack;

        if (needle == null) {
            if (ModuleManager.IsModuleEnabled(ModulesEnum.trees)) {
                needle = new ItemStack(JaffasTrees.itemUnknownSeeds);
            } else {
                return stack;
            }
        }

        if (player.isSneaking()) {
            int seedsLeft = MAXIMAL_IDS_PER_RUN;
            while (tryIdentify(stack, player) && seedsLeft-- > 0) ;
        } else {
            tryIdentify(stack, player);
        }

        return stack;
    }

    private boolean tryIdentify(ItemStack stack, EntityPlayer player) {
        if (stack.stackSize <= 0) return false;
        ItemStackInInventory item = InventoryUtils.findFirstMatchingItem(player.inventory, needle);
        if (item == null) return false;

        if (item.decreaseStackSize()) {
            PlayerHelper.giveItemToPlayer(player, SeedsHelper.getRandomSeed());
            PlayerHelper.damageCurrentItem(player);
            return true;
        }

        return false;
    }
}
