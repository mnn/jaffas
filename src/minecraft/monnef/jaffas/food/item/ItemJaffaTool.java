/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class ItemJaffaTool extends ItemJaffaBase {
    public float efficiencyOnProperMaterial = 4.0F;
    public int damageVsEntity;
    protected EnumToolMaterial toolMaterial;
    protected int durabilityLossOnEntityHit = 2;
    protected int durabilityLossOnBlockBreak = 1;
    protected boolean disableRepairing = true;

    public ItemJaffaTool(int id, int textureIndex, EnumToolMaterial material) {
        super(id, textureIndex);
        this.toolMaterial = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
        this.damageVsEntity = material.getDamageVsEntity();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
        damageTool(durabilityLossOnEntityHit, par3EntityLiving, stack);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving) {
        if ((double) Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6) != 0.0D) {
            damageTool(durabilityLossOnBlockBreak, par7EntityLiving, par1ItemStack);
        }

        return true;
    }

    @Override
    public int getDamageVsEntity(Entity par1Entity) {
        return this.damageVsEntity;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    public String getToolMaterialName() {
        return this.toolMaterial.toString();
    }

    @Override
    public final float getStrVsBlock(ItemStack stack, Block block, int meta) {
        if (nearlyDestroyed(stack)) {
            return 0;
        }

        if (ForgeHooks.isToolEffective(stack, block, meta)) {
            return efficiencyOnProperMaterial;
        }
        return getCustomStrVsBlock(stack, block, meta);
    }

    protected float getCustomStrVsBlock(ItemStack stack, Block block, int meta) {
        return super.getStrVsBlock(stack, block, meta);
    }


    protected boolean nearlyDestroyed(ItemStack stack) {
        return stack.getMaxDamage() == stack.getItemDamage();
    }

    protected void damageTool(int dmg, EntityLiving source, ItemStack stack) {
        int newDmg = stack.getItemDamage() + dmg;
        if (newDmg == stack.getMaxDamage()) {
            if (source != null) {
                source.renderBrokenItemStack(stack);
            }
        }
        if (newDmg > stack.getMaxDamage()) {
            dmg = stack.getMaxDamage() - stack.getItemDamage();
        }
        stack.damageItem(dmg, source);
    }

    @Override
    public int getItemEnchantability() {
        return this.toolMaterial.getEnchantability();
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material) {
        if (this.disableRepairing) return false;
        return this.toolMaterial.getToolCraftingMaterial() == material.itemID || super.getIsRepairable(stack, material);
    }

    @Override
    public int getDamageVsEntity(Entity entity, ItemStack itemStack) {
        if (nearlyDestroyed(itemStack)) {
            return 0;
        }
        return getDamageVsEntity(entity);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(stack, player, list, par4);
        if (nearlyDestroyed(stack)) list.add("Broken");
    }
}
