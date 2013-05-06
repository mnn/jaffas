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

import java.util.List;

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

    private boolean nearlyDestroyed(ItemStack stack) {
        return stack.getMaxDamage() == stack.getItemDamage();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
        damageSword(1, par3EntityLiving, stack);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving) {
        if ((double) Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6) != 0.0D) {
            damageSword(4, par7EntityLiving, par1ItemStack);
        }

        return true;
    }

    private void damageSword(int dmg, EntityLiving source, ItemStack stack) {
        int newDmg = stack.getItemDamage() + dmg;
        if (newDmg > stack.getMaxDamage()) dmg = stack.getMaxDamage() - stack.getItemDamage();
        stack.damageItem(dmg, source);
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
    public boolean getIsRepairable(ItemStack stack, ItemStack material) {
        return this.toolMaterial.getToolCraftingMaterial() == material.itemID ? true : super.getIsRepairable(stack, material);
    }

    @Override
    public int getDamageVsEntity(Entity entity, ItemStack itemStack) {
        if (nearlyDestroyed(itemStack)) {
            return 1;
        }
        return getDamageVsEntity(entity);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(stack, player, list, par4);
        if (nearlyDestroyed(stack)) list.add("Broken");
    }
}
