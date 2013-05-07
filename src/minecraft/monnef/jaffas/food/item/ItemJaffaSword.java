/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemJaffaSword extends ItemJaffaTool {

    public ItemJaffaSword(int ItemID, int textureOffset, EnumToolMaterial material) {
        super(ItemID, textureOffset, material);
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.durabilityLossOnBlockBreak = 4;
        this.durabilityLossOnEntityHit = 1;
        this.damageVsEntity += 4;
    }

    // ---------------
    // mostly from ItemSword

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        if (nearlyDestroyed(stack)) return 1f;

        if (block.blockID == Block.web.blockID) {
            return 15.0F;
        } else {
            Material material = block.blockMaterial;
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.pumpkin ? 1.0F : 1.5F;
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
    public boolean canHarvestBlock(Block par1Block) {
        return par1Block.blockID == Block.web.blockID;
    }

}
