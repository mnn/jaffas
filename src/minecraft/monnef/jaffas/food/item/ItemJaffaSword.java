/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemJaffaSword extends ItemJaffaBase {
    private int weaponDamage;
    private final EnumToolMaterial toolMaterial;

    public ItemJaffaSword(int ItemID, int textureOffset, EnumToolMaterial material) {
        super(ItemID);
        this.toolMaterial = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.weaponDamage = 4 + material.getDamageVsEntity();
        this.setCustomIconIndex(textureOffset);
    }

    // TODO: remove?
    public int func_82803_g() {
        return this.toolMaterial.getDamageVsEntity();
    }

    // ---------------
    // mostly from ItemSword

    @Override
    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
        if (par2Block.blockID == Block.web.blockID) {
            return 15.0F;
        } else {
            Material material = par2Block.blockMaterial;
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.pumpkin ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
        par1ItemStack.damageItem(1, par3EntityLiving);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving) {
        if ((double) Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6) != 0.0D) {
            par1ItemStack.damageItem(2, par7EntityLiving);
        }

        return true;
    }

    @Override
    public int getDamageVsEntity(Entity par1Entity) {
        return this.weaponDamage;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
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

    @Override
    public int getItemEnchantability() {
        return this.toolMaterial.getEnchantability();
    }

    // remove?
    public String getToolMaterialName() {
        return this.toolMaterial.toString();
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return this.toolMaterial.getToolCraftingMaterial() == par2ItemStack.itemID ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

}
