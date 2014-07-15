/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.api.ICustomIcon;
import monnef.core.common.CustomIconHelper;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;

import java.util.List;

public class ItemFruitSeeds extends ItemBlockTrees implements IFactoryPlantable {
    public int serialNumber = -1;
    private int subCount;
    private boolean firstInSequence = false;
    private Icon[] icons;

    public ItemFruitSeeds(int itemID, int blockID, int textureOffset, int subCount) {
        super(itemID, blockID);
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
    public Icon getIconFromDamage(int meta) {
        return icons[meta];
    }

    @Override
    public void registerIcons(IconRegister register) {
        icons = new Icon[subCount];
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
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < subCount; i++) {
            if (firstInSequence && i == 0) continue;
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    public void setFirstInSequence() {
        this.firstInSequence = true;
    }

    @Override
    public int getSeedId() {
        return itemID;
    }

    @Override
    public int getPlantedBlockId(World world, int x, int y, int z, ItemStack stack) {
        return getBlockID();
    }

    @Override
    public int getPlantedBlockMetadata(World world, int x, int y, int z, ItemStack stack) {
        return stack.getItemDamage();
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        return world.getBlockId(x, y, z) == 0 && getBlock().canBlockStay(world, x, y, z);
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
    }
}
