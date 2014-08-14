/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.api.ICustomIcon;
import monnef.core.common.CustomIconHelper;
import monnef.jaffas.food.common.MfrHelper;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import powercrystals.minefactoryreloaded.api.ReplacementBlock;

import java.util.List;

public class ItemFruitSeeds extends ItemBlockTrees implements IFactoryPlantable {
    public int serialNumber = -1;
    private int subCount;
    private boolean firstInSequence = false;
    private IIcon[] icons;

    public ItemFruitSeeds(Block block, int textureOffset, int subCount) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        setCustomIconIndex(textureOffset);
        this.subCount = subCount;
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return icons[meta];
    }

    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[subCount];
        for (int i = 0; i < icons.length; i++) {
            if (firstInSequence && i == 0) continue;
            icons[i] = register.registerIcon(CustomIconHelper.generateShiftedId((ICustomIcon) this, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int dmg = par1ItemStack.getItemDamage();

        if (dmg < 0 || dmg >= JaffasTrees.treeTypes.length) {
            dmg = 0;
        }

        return super.getUnlocalizedName() + "." + dmg;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List result) {
        for (int i = 0; i < subCount; i++) {
            if (firstInSequence && i == 0) continue;
            result.add(new ItemStack(item, 1, i));
        }
    }

    public void setFirstInSequence() {
        this.firstInSequence = true;
    }

    @Override
    public Item getSeed() {
        return this;
    }

    @Override
    public boolean canBePlanted(ItemStack stack, boolean forFermenting) {
        return true;
    }

    @Override
    public ReplacementBlock getPlantedBlock(World world, int x, int y, int z, ItemStack stack) {
        return MfrHelper.replacementBlockWithMeta(getBlock(), stack.getItemDamage());
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        return world.isAirBlock(x, y, z) && getBlock().canBlockStay(world, x, y, z);
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
    }
}
