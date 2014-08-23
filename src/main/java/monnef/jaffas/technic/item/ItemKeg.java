/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.utils.RegistryUtils;
import monnef.jaffas.technic.block.TileKeg;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import static monnef.core.utils.BlockHelper.setBlock;
import static monnef.core.utils.StringsHelper.makeFirstCapital;
import static monnef.jaffas.technic.JaffasTechnic.keg;

public class ItemKeg extends ItemTechnic {
    protected String[] subNames;
    protected String[] subTitles;

    public ItemKeg(int textureIndex) {
        super(textureIndex);
        subNames = new String[TileKeg.KegType.values().length];
        subTitles = new String[TileKeg.KegType.values().length];

        for (int i = 0; i < TileKeg.KegType.values().length; i++) {
            TileKeg.KegType kegType = TileKeg.KegType.values()[i];
            subNames[i] = "keg" + makeFirstCapital(kegType.toString().toLowerCase());
            subTitles[i] = "Keg - " + makeFirstCapital(kegType.getTitle());
        }

        setHasSubtypes(true);
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        } else if (side != 1) {
            //not the top of a block
            return false;
        } else {
            Block activatedBlock = world.getBlock(x, y, z);
            boolean replacing = false;

            if (activatedBlock != null && activatedBlock.isReplaceable(world, x, y, z)) {
                replacing = true;
            } else {
                y++;
            }

            Block blockToPlace = keg;
            //int direction = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            //direction = (direction + 3) % 4;

            if (player.canPlayerEdit(x, y, z, side, item)) {
                if (world.isAirBlock(x, y, z) || replacing) {
                    if (world.isAirBlock(x, y + 1, z)) {
                        setBlock(world, x, y, z, blockToPlace, 0);

                        --item.stackSize;

                        TileKeg tile = (TileKeg) world.getTileEntity(x, y, z);
                        tile.initNewKeg(TileKeg.KegType.values()[item.getItemDamage()]);
                        return true;

                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < TileKeg.KegType.values().length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (subNames == null) {
            throw new NullPointerException("subnames not set!");
        }
        String subName = itemstack.getItemDamage() >= subNames.length ? "STRING NOT FOUND" : subNames[itemstack.getItemDamage()];
        return getUnlocalizedName() + "." + subName;
    }
}
