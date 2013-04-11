/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.api.IItemBlock;
import monnef.jaffas.food.item.ItemJaffaBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;

public class ItemBlockJaffas extends ItemJaffaBase implements IItemBlock {
    protected String[] subNames;
    protected boolean useItemName = false;

    public ItemBlockJaffas(int par1) {
        super(par1);
        setHasSubtypes(true);
        this.blockID = par1 + 256;
    }

    public ItemBlockJaffas(int id, int blockId) {
        this(id);
        this.blockID = blockId;
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

    // from ItemBlock
    private int blockID;
    @SideOnly(Side.CLIENT)
    private Icon field_94588_b;

    public int getBlockID() {
        return this.blockID;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getSpriteNumber() {
        return Block.blocksList[this.blockID].func_94327_t_() != null ? 1 : 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1) {
        return this.field_94588_b != null ? this.field_94588_b : Block.blocksList[this.blockID].getBlockTextureFromSideAndMetadata(1, par1);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        int i1 = par3World.getBlockId(par4, par5, par6);

        if (i1 == Block.snow.blockID && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1) {
            par7 = 1;
        } else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
                && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6))) {
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
            Block block = Block.blocksList[this.blockID];
            int j1 = this.getMetadata(par1ItemStack.getItemDamage());
            int k1 = Block.blocksList[this.blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, j1);

            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, k1)) {
                par3World.playSoundEffect((double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F), (double) ((float) par6 + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                --par1ItemStack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    // not called for custom ItemBlock :(
    // remove or move to onItemUse?
    public boolean canPlaceItemBlockOnSide(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack) {
        int i1 = par1World.getBlockId(par2, par3, par4);

        if (i1 == Block.snow.blockID) {
            par5 = 1;
        } else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
                && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(par1World, par2, par3, par4))) {
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

    @Override
    public String getUnlocalizedName() {
        return useItemName ? super.getUnlocalizedName() : Block.blocksList[this.blockID].getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTab() {
        return Block.blocksList[this.blockID].getCreativeTabToDisplayOn();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        Block.blocksList[this.blockID].getSubBlocks(par1, par2CreativeTabs, par3List);
    }

    @SideOnly(Side.CLIENT)
    public void updateIcons(IconRegister par1IconRegister) {
        String s = Block.blocksList[this.blockID].func_94327_t_();

        if (s != null) {
            this.field_94588_b = par1IconRegister.registerIcon(s);
        }
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, this.blockID, metadata, 3)) {
            return false;
        }

        if (world.getBlockId(x, y, z) == this.blockID) {
            Block.blocksList[this.blockID].onBlockPlacedBy(world, x, y, z, player, stack);
            Block.blocksList[this.blockID].onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }

    @Override
    public int getBlockIdCustom() {
        return this.getBlockID();
    }
}
