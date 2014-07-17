/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class ItemJaffaTool extends ItemJaffaBase {
    public float efficiencyOnProperMaterial = 4.0F;
    public float damageVsEntity;
    protected ToolMaterial toolMaterial;
    protected int durabilityLossOnEntityHit = 2;
    protected int durabilityLossOnBlockBreak = 1;
    protected boolean disableRepairing = true;

    public ItemJaffaTool(int textureIndex, Item.ToolMaterial material) {
        super(textureIndex);
        this.toolMaterial = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
        this.damageVsEntity = material.getDamageVsEntity();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
        damageTool(durabilityLossOnEntityHit, par3EntityLiving, stack);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if ((double) block.getBlockHardness(world, x, y, z) != 0.0D) {
            damageTool(durabilityLossOnBlockBreak, entity, stack);
        }

        return true;
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack) {
        Multimap multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double) this.damageVsEntity, 0));
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    private static final int UNKNOWN_METADATA = 0;

    /**
     * getStrVsBlock, do not override unless you need really special behaviour.
     * In that case don't forget to implement nearlyDestroyed state.
     *
     * @param stack Tool.
     * @param block Used on this.
     * @return Effectiveness.
     */

    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        if (nearlyDestroyed(stack)) {
            return 0;
        }

        if (ForgeHooks.isToolEffective(stack, block, UNKNOWN_METADATA)) {
            return efficiencyOnProperMaterial;
        }
        return getCustomStrVsBlock(stack, block);
    }

    protected float getCustomStrVsBlock(ItemStack stack, Block block) {
        return super.func_150893_a(stack, block);
    }

    public static boolean nearlyDestroyed(ItemStack stack) {
        return stack.getMaxDamage() == stack.getItemDamage();
    }

    protected void damageTool(int dmg, EntityLivingBase source, ItemStack stack) {
        int newDmg = stack.getItemDamage() + dmg;
        if (newDmg == stack.getMaxDamage()) {
            if (source != null) {
                source.renderBrokenItemStack(stack);
                refreshDamageAttribute(stack);
            }
        }
        if (newDmg > stack.getMaxDamage()) {
            dmg = stack.getMaxDamage() - stack.getItemDamage();
        }
        stack.damageItem(dmg, source);
    }

    private void refreshDamageAttribute(ItemStack stack) {
        NBTTagCompound inbt = stack.stackTagCompound;
        NBTTagCompound nnbt = new NBTTagCompound();
        NBTTagList nnbtl = new NBTTagList();
        AttributeModifier att = new AttributeModifier(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), getCustomDamageVsEntity(stack), 0);
        nnbt.setLong("UUIDMost", att.getID().getMostSignificantBits());
        nnbt.setLong("UUIDLeast", att.getID().getLeastSignificantBits());
        nnbt.setString("Name", att.getName());
        nnbt.setDouble("Amount", att.getAmount());
        nnbt.setInteger("Operation", att.getOperation());
        nnbt.setString("AttributeName", att.getName());
        nnbtl.appendTag(nnbt);
        inbt.setTag("AttributeModifiers", nnbtl);
    }

    @Override
    public int getItemEnchantability() {
        return this.toolMaterial.getEnchantability();
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material) {
        if (this.disableRepairing) return false;
        return this.toolMaterial.customCraftingMaterial == material.getItem() || super.getIsRepairable(stack, material);
    }

    @Override
    public int getCustomDamageVsEntity(ItemStack itemStack) {
        if (nearlyDestroyed(itemStack)) {
            return 0;
        }
        return getCustomDamageVsEntity();
    }

    @Override
    public void addInformationCustom(ItemStack stack, EntityPlayer player, List<String> result, boolean par4) {
        super.addInformationCustom(stack, player, result, par4);
        if (nearlyDestroyed(stack)) result.add("Broken");
    }
}
