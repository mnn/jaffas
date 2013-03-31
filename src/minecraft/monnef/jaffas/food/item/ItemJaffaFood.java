package monnef.jaffas.food.item;

import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.item.common.IItemFood;
import monnef.jaffas.food.jaffasFood;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemJaffaFood extends ItemJaffaBase implements IItemFood {
    private ItemStack returnItem;
    private boolean isDrink;
    private int healAmount;
    private float saturation;
    private float returnChance;

    public ItemJaffaFood(int id) {
        super(id);
        initialize();
    }

    private void initialize() {
        maxStackSize = 64;
        this.itemUseDuration = 32;
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{jaffasFood.CreativeTab, CreativeTabs.tabFood};
    }

    public void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if (this.returnItem != null && this.returnChance > itemRand.nextFloat()) {
            PlayerHelper.giveItemToPlayer(player, returnItem.copy());
        }

        if (!world.isRemote && this.potionId > 0 && world.rand.nextFloat() < this.potionEffectProbability) {
            player.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
        }
    }

    public ItemJaffaFood setReturnItem(ItemStack returnItem) {
        setReturnItem(returnItem, 1f);
        return this;
    }

    public ItemJaffaFood setReturnItem(ItemStack returnItem, float chance) {
        this.returnItem = returnItem;
        this.returnChance = chance;
        return this;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return this.isDrink ? EnumAction.drink : EnumAction.eat;
    }

    public ItemJaffaFood setIsDrink() {
        this.isDrink = true;
        return this;
    }

    @Override
    public Item Setup(int healAmount, float saturation) {
        this.healAmount = healAmount;
        this.saturation = saturation;

        return this;
    }

    public int getHealAmount() {
        return this.healAmount;
    }

    public float getSaturationModifier() {
        return this.saturation;
    }

    // ---------------------------------------------------------
    // from ItemFood

    private int itemUseDuration;

    private boolean alwaysEdible;

    private int potionId;
    private int potionDuration;
    private int potionAmplifier;
    private float potionEffectProbability;

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        --par1ItemStack.stackSize;
        par3EntityPlayer.getFoodStats().addStats(this.getHealAmount(), this.getSaturationModifier());
        par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        return par1ItemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return this.itemUseDuration;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par3EntityPlayer.canEat(this.alwaysEdible)) {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    public ItemJaffaFood setPotionEffect(int par1, int par2, int par3, float par4) {
        this.potionId = par1;
        this.potionDuration = par2;
        this.potionAmplifier = par3;
        this.potionEffectProbability = par4;
        return this;
    }

    public ItemJaffaFood setAlwaysEdible() {
        this.alwaysEdible = true;
        return this;
    }
}
