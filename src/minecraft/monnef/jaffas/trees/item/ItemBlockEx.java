package monnef.jaffas.trees.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.trees.mod_jaffas_trees;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static monnef.core.utils.BlockHelper.setBlock;

public class ItemBlockEx extends ItemTrees {
    /**
     * The block ID of the Block associated with this ItemBlock
     */
    private int blockID;

    public ItemBlockEx(int itemID, int blockID) {
        super(itemID);
        this.blockID = blockID;
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }

    /**
     * Returns the blockID for this Item
     */
    public int getBlockID() {
        return this.blockID;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        int var11 = par3World.getBlockId(par4, par5, par6);

        if (var11 == Block.snow.blockID) {
            par7 = 1;
        } else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID
                && (Block.blocksList[var11] == null || !Block.blocksList[var11].isBlockReplaceable(par3World, par4, par5, par6))) {
            if (par7 == 0) {
                --par5;
            }

            if (par7 == 1) {
                ++par5;
            }

            if (par7 == 2) {
                --par6;
            }

            if (par7 == 3) {
                ++par6;
            }

            if (par7 == 4) {
                --par4;
            }

            if (par7 == 5) {
                ++par4;
            }
        }

        if (par1ItemStack.stackSize == 0) {
            return false;
        } else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
            return false;
        } else if (par5 == 255 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
            return false;
        } else if (par3World.canPlaceEntityOnSide(this.blockID, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack)) {
            Block var12 = Block.blocksList[this.blockID];
            int var13 = this.getMetadata(par1ItemStack.getItemDamage());
            int var14 = Block.blocksList[this.blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, var13);

            //if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10))
            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, var14)) {
                par3World.playSoundEffect((double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F), (double) ((float) par6 + 0.5F), var12.stepSound.getPlaceSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
                --par1ItemStack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns true if the given ItemBlock can be placed on the given side of the given block position.
     */
    public boolean canPlaceItemBlockOnSide(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack) {
        int var8 = par1World.getBlockId(par2, par3, par4);

        if (var8 == Block.snow.blockID) {
            par5 = 1;
        } else if (var8 != Block.vine.blockID && var8 != Block.tallGrass.blockID && var8 != Block.deadBush.blockID
                && (Block.blocksList[var8] == null || !Block.blocksList[var8].isBlockReplaceable(par1World, par2, par3, par4))) {
            if (par5 == 0) {
                --par3;
            }

            if (par5 == 1) {
                ++par3;
            }

            if (par5 == 2) {
                --par4;
            }

            if (par5 == 3) {
                ++par4;
            }

            if (par5 == 4) {
                --par2;
            }

            if (par5 == 5) {
                ++par2;
            }
        }

        return par1World.canPlaceEntityOnSide(this.getBlockID(), par2, par3, par4, false, par5, (Entity) null, par7ItemStack);
    }

    @SideOnly(Side.CLIENT)
    /**
     * gets the CreativeTab this item is displayed on
     */
    public CreativeTabs getCreativeTab() {
        return Block.blocksList[this.blockID].getCreativeTabToDisplayOn();
    }

    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param stack  The item stack that was used to place the block. This can be changed inside the method.
     * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side   The side the player (or machine) right-clicked on.
     */
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!setBlock(world, x, y, z, this.blockID, metadata)) {
            return false;
        }

        if (world.getBlockId(x, y, z) == this.blockID) {
            Block.blocksList[this.blockID].onBlockPlacedBy(world, x, y, z, player, stack);
            Block.blocksList[this.blockID].onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }
}
