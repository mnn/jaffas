package monnef.jaffas.trees;

import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

public class ItemJaffaSeeds extends ItemSeeds {
    public ItemJaffaSeeds(int id, int blockId, int soilBlockId) {
        super(id, blockId, soilBlockId);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    public String getTextureFile() {
        return "/jaffas_02.png";
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par7 != 1) {
            return false;
        } else if (par2EntityPlayer == null || (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack))) {
            int var11 = par3World.getBlockId(par4, par5, par6);
            Block soil = Block.blocksList[var11];

            if (soil != null && soil.canSustainPlant(par3World, par4, par5, par6, ForgeDirection.UP, this) && par3World.isAirBlock(par4, par5 + 1, par6)) {
                par3World.setBlockWithNotify(par4, par5 + 1, par6, this.getPlantID(null, 0, 0, 0));
                --par1ItemStack.stackSize;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
