/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.utils.BlockHelper;
import monnef.core.utils.StringsHelper;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.TileFungiBox;
import monnef.jaffas.technic.common.FungiCatalog;
import monnef.jaffas.technic.common.FungusInfo;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import powercrystals.minefactoryreloaded.api.ReplacementBlock;

import java.util.List;
import java.util.Map;

public class ItemFungus extends ItemTechnic implements IFactoryPlantable {
    public ItemFungus(int textureIndex) {
        super(textureIndex);
        setIconsCount(FungiCatalog.getMaxId() + 1); // maximal id to count
        setHasSubtypes(true);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return getCustomIcon(meta);
    }

    @Override
    public void addInformationCustom(ItemStack stack, EntityPlayer player, List result, boolean par4) {
        super.addInformationCustom(stack, player, result, par4);
        FungusInfo info = FungiCatalog.get(stack.getItemDamage());
        String subTitle = info.subTitle;
        if (subTitle != null && !subTitle.isEmpty()) {
            result.add(subTitle);
        }
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs tabs, List list) {
        for (Map.Entry<Integer, FungusInfo> fungus : FungiCatalog.catalog.entrySet()) {
            if (fungus.getValue().ordinalItemBind) {
                list.add(new ItemStack(this, 1, fungus.getKey()));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        FungusInfo info = FungiCatalog.get(itemstack.getItemDamage());
        if (info == null) return getUnlocalizedName();
        String subName = StringsHelper.generateNameFromTitle(info.title);
        return getUnlocalizedName() + "." + subName;
    }

    // MFR
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
        return new ReplacementBlock(Blocks.dirt);
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        Block block = world.getBlock(x, y - 1, z);
        if (block != JaffasTechnic.fungiBox) return false;
        TileFungiBox tile = (TileFungiBox) world.getTileEntity(x, y - 1, z);
        return !tile.mushroomPlanted();
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
        Block block = world.getBlock(x, y - 1, z);
        BlockHelper.setAir(world, x, y, z);
        if (block != JaffasTechnic.fungiBox) return;
        TileFungiBox tile = (TileFungiBox) world.getTileEntity(x, y - 1, z);
        tile.tryPlant(stack);
    }
}
