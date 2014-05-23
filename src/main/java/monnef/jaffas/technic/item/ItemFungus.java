/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.TileFungiBox;
import monnef.jaffas.technic.common.FungiCatalog;
import monnef.jaffas.technic.common.FungusInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;

import java.util.List;
import java.util.Map;

public class ItemFungus extends ItemTechnic implements IFactoryPlantable {
    public ItemFungus(int id, int textureIndex) {
        super(id, textureIndex);
        setIconsCount(FungiCatalog.getMaxId() + 1); // maximal id to count
        setHasSubtypes(true);
    }

    @Override
    public Icon getIconFromDamage(int meta) {
        return getCustomIcon(meta);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List result, boolean par4) {
        super.addInformation(stack, player, result, par4);
        FungusInfo info = FungiCatalog.get(stack.getItemDamage());
        String subTitle = info.subTitle;
        if (subTitle != null && !subTitle.isEmpty()) {
            result.add(subTitle);
        }
    }

    @Override
    public void getSubItems(int par1, CreativeTabs tabs, List list) {
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
        String subName = info.title.toLowerCase();
        return getUnlocalizedName() + "." + subName;
    }

    // MFR
    @Override
    public int getSeedId() {
        return itemID;
    }

    @Override
    public int getPlantedBlockId(World world, int x, int y, int z, ItemStack stack) {
        return 1;
    }

    @Override
    public int getPlantedBlockMetadata(World world, int x, int y, int z, ItemStack stack) {
        return 1;
    }

    @Override
    public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
        int id = world.getBlockId(x, y - 1, z);
        if (id != JaffasTechnic.fungiBox.blockID) return false;
        TileFungiBox tile = (TileFungiBox) world.getBlockTileEntity(x, y - 1, z);
        return !tile.mushroomPlanted();
    }

    @Override
    public void prePlant(World world, int x, int y, int z, ItemStack stack) {
    }

    @Override
    public void postPlant(World world, int x, int y, int z, ItemStack stack) {
        int id = world.getBlockId(x, y - 1, z);
        world.setBlock(x, y, z, 0);
        if (id != JaffasTechnic.fungiBox.blockID) return;
        TileFungiBox tile = (TileFungiBox) world.getBlockTileEntity(x, y - 1, z);
        tile.tryPlant(stack);
    }
}
