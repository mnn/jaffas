/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemJaffaSword extends ItemJaffaTool {

    public ItemJaffaSword(int textureOffset, Item.ToolMaterial material) {
        super(textureOffset, material);
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.durabilityLossOnBlockBreak = 4;
        this.durabilityLossOnEntityHit = 1;
        this.damageVsEntity += 4;
    }

    // ---------------
    // mostly from ItemSword

    // getStrVsBlock
    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        if (nearlyDestroyed(stack)) return 1f;

        if (block == Blocks.web) {
            return 15.0F;
        } else {
            Material material = block.getMaterial();
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack stack) {
        return par1Block == Blocks.web;
    }
}
