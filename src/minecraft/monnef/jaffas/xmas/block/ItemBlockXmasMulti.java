package monnef.jaffas.xmas.block;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public abstract class ItemBlockXmasMulti extends ItemBlock {
    private String[] subNames;
    private String[] subTitles;

    public ItemBlockXmasMulti(int id) {
        super(id);
        setHasSubtypes(true);
        this.subNames = this.getSubNames();
        this.subTitles = this.getSubTitles();
        this.registerNames(getParentBlock());
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getItemDisplayName(ItemStack itemstack) {
        return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
    }

    public int getSubBlocksCount() {
        return this.subNames.length;
    }

    public void registerNames(BlockXmasMulti block) {
        for (int i = 0; i < subNames.length; i++) {
            ItemStack multiBlockStack = new ItemStack(block, 1, i);
            LanguageRegistry.addName(multiBlockStack, subTitles[multiBlockStack.getItemDamage()]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, subNames.length);
        //TODO
        // return this.iconIndex + var2;
        return null;
    }

    protected abstract String[] getSubNames();

    protected abstract String[] getSubTitles();

    protected abstract BlockXmasMulti getParentBlock();
}
