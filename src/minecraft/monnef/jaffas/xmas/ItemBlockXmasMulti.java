package monnef.jaffas.xmas;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;

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
    public String getItemNameIS(ItemStack itemstack) {
        return getItemName() + "." + subNames[itemstack.getItemDamage()];
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
    /**
     * Gets an icon index based on an item's damage value
     */
    public int getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, subNames.length);
        return this.iconIndex + var2;
    }

    protected abstract String[] getSubNames();

    protected abstract String[] getSubTitles();

    protected abstract BlockXmasMulti getParentBlock();
}
