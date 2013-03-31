package monnef.jaffas.food.block;

public class ItemBlockSwitchgrass extends ItemBlockJaffas {
    public ItemBlockSwitchgrass(int par1) {
        super(par1);
        subNames = new String[16];
        for (int i = 0; i < subNames.length; i++) {
            subNames[i] = "sg" + i;
        }
        subNames[15] = "top";

        setUnlocalizedName("itemBlockJSwitchgrass");
    }

    /*
    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getLocalizedName(ItemStack itemstack) {
        return getUnlocalizedName() + "." + subNames[0];
    }

    @Override
    public String getItemDisplayName(ItemStack par1ItemStack) {
        return super.getItemDisplayName(par1ItemStack);
    }
    */
}