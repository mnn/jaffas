/*
 * Jaffas and more!
 * author: monnef
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
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class ItemBlockJaffas extends ItemJaffaBase implements IItemBlock {
    protected String[] subNames;
    protected boolean useItemName = false;

    public ItemBlockJaffas(int id) {
        super(id);
        setHasSubtypes(true);
        this.blockID = id + 256;
    }

    public ItemBlockJaffas(int id, int blockId) {
        this(id);
        this.blockID = blockId;
    }

    @Override
    public void setSubNames(String[] newNames) {
        subNames = newNames;
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
    private Icon iconOfMyBlock;

    public int getBlockID() {
        return this.blockID;
    }

    public Block getBlock() {
        return Block.blocksList[getBlockID()];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getSpriteNumber() {
        return Block.blocksList[this.blockID].getItemIconName() != null ? 1 : 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1) {
        return this.iconOfMyBlock != null ? this.iconOfMyBlock : Block.blocksList[this.blockID].getIcon(1, par1);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        int i1 = world.getBlockId(x, y, z);

        if (i1 == Block.snow.blockID && (world.getBlockMetadata(x, y, z) & 7) < 1) {
            side = 1;
        } else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
                && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(world, x, y, z))) {
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            x += dir.offsetX;
            y += dir.offsetY;
            z += dir.offsetZ;
        }

        if (stack.stackSize == 0) {
            return false;
        } else if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        } else if (y == 255 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
            return false;
        } else if (world.canPlaceEntityOnSide(this.blockID, x, y, z, false, side, player, stack)) {
            Block block = Block.blocksList[this.blockID];
            int j1 = this.getMetadata(stack.getItemDamage());
            int k1 = Block.blocksList[this.blockID].onBlockPlaced(world, x, y, z, side, par8, par9, par10, j1);

            if (placeBlockAt(stack, player, world, x, y, z, side, par8, par9, par10, k1)) {
                world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                --stack.stackSize;
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
    @Override
    public void registerIcons(IconRegister par1IconRegister) {
        String s = Block.blocksList[this.blockID].getItemIconName();

        if (s != null) {
            this.iconOfMyBlock = par1IconRegister.registerIcon(s);
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
