/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.MonnefCorePlugin;
import monnef.jaffas.food.item.ItemJaffaSword;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemSwordTechnic extends ItemJaffaSword {
    public ItemSwordTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
        setCreativeTab(JaffasTechnic.instance.creativeTab);
        setSecondCreativeTab(CreativeTabs.tabCombat);
        setSheetNumber(3);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        if (MonnefCorePlugin.debugEnv) par3List.add(new ItemStack(par1, 1, getMaxDamage() - 10));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (!nearlyDestroyed(stack)) {
            int blockId = world.getBlockId(x, y, z);
            if (blockId == Block.web.blockID) {
                if (!world.isRemote) {
                    damageTool(2, player, stack);
                    world.destroyBlock(x, y, z, true);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
