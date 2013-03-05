package monnef.jaffas.food.item;

import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.item.common.IItemFood;
import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemJaffaFood extends ItemFood implements IItemFood {

    private ItemStack returnItem;
    private boolean isDrink;
    private int healAmount;
    private float saturation;
    protected int textureFileIndex;
    private float returnChance;

    public ItemJaffaFood(int id) {
        super(id, 0, 0, false);
        initialize();
    }

    public ItemJaffaFood(int id, int healAmount, float saturation) {
        //super(id, healAmount, saturation, false);
        super(id, 0, 0, false);
        initialize();
    }

    private void initialize() {
        maxStackSize = 64;
        this.setCreativeTab(CreativeTabs.tabFood);
        this.setCreativeTab(mod_jaffas_food.CreativeTab);
    }

    @Override
    public String getTextureFile() {
        return mod_jaffas_food.textureFile[getTextureFileIndex()];
    }

    public int getTextureFileIndex() {
        return textureFileIndex;
    }

    public void setTextureFileIndex(int value) {
        textureFileIndex = value;
    }

    @Override
    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (this.returnItem != null && this.returnChance > itemRand.nextFloat()) {
            PlayerHelper.giveItemToPlayer(par3EntityPlayer, returnItem.copy());
        }

        return super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    public ItemJaffaFood setReturnItem(ItemStack returnItem) {
        setReturnItem(returnItem, 1f);
        return this;
    }

    public ItemFood setReturnItem(ItemStack returnItem, float chance) {
        this.returnItem = returnItem;
        this.returnChance = chance;
        return this;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
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

    @Override
    public int getHealAmount() {
        return this.healAmount;
    }

    @Override
    public float getSaturationModifier() {
        return this.saturation;
    }
}
