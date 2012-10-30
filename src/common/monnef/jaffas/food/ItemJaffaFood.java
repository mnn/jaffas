package monnef.jaffas.food;

import net.minecraft.src.*;

public class ItemJaffaFood extends ItemFood {

    private ItemStack returnItem;

    public ItemJaffaFood(int id, int healAmount, float saturation) {
        super(id, healAmount, saturation, false);
        maxStackSize = 64;
        this.setTabToDisplayOn(CreativeTabs.tabFood);
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
}
