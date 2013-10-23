/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.common.CustomIconHelper;
import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.xmas.common.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public abstract class ItemBlockXmasMulti extends ItemBlockJaffas {
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
        return icons[var2];
    }

    @Override
    public String getDefaultModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 4;
    }

    public abstract String[] getSubNames();

    public abstract String[] getSubTitles();

    public abstract BlockXmasMulti getParentBlock();

    public Icon[] icons;

    @Override
    public void registerIcons(IconRegister register) {
        icons = new Icon[getSubBlocksCount()];
        for (int i = 0; i < icons.length; i++) {
            icons[i] = register.registerIcon(CustomIconHelper.generateShiftedId(this, i));
        }
    }
}
