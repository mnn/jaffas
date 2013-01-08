package monnef.jaffas.food;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
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
        this.setCreativeTab(mod_jaffas.CreativeTab);
    }

    public String getTextureFile() {
        return "/jaffas_01.png";
    }

    @Override
    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (this.returnItem != null) {
            EntityItem e = new EntityItem(par2World, par3EntityPlayer.posX, par3EntityPlayer.posY + 1, par3EntityPlayer.posZ, returnItem.copy());
            if (!par2World.isRemote) {
                par2World.spawnEntityInWorld(e);
            }
        }

        return super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    public ItemJaffaFood setReturnItem(ItemStack returnItem) {
        this.returnItem = returnItem;
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
