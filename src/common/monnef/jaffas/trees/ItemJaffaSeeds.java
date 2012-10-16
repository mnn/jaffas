package monnef.jaffas.trees;

import net.minecraft.src.*;

public class ItemJaffaSeeds extends Item {
    /**
     * The type of block this seed turns into (wheat or pumpkin stems for instance)
     */
    private int blockType;

    /**
     * BlockID of the block the seeds can be planted on.
     */
    private int soilBlockID;

    public ItemJaffaSeeds(int id, int blockId, int soilBlockId) {
        super(id);
        this.blockType = blockId;
        this.soilBlockID = soilBlockId;
        this.setTabToDisplayOn(CreativeTabs.tabMaterials);
    }

    public String getTextureFile() {
        return "/jaffas_02.png";
    }

    public boolean tryPlaceIntoWorld(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
        if (par7 != 1) {
            return false;
        } else if (player.canPlayerEdit(x, y, z) && player.canPlayerEdit(x, y + 1, z)) {
            int var11 = world.getBlockId(x, y, z);

            if (var11 == this.soilBlockID && world.isAirBlock(x, y + 1, z)) {
                world.setBlockWithNotify(x, y + 1, z, this.blockType);
                --stack.stackSize;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
