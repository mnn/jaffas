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
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ItemBlockJaffas extends ItemJaffaBase implements IItemBlock {
    protected String[] subNames;
    protected boolean useItemName = false;

    public ItemBlockJaffas(Block block) {
        super();
        setHasSubtypes(true);
        this.block = block;
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
    private Block block;
    @SideOnly(Side.CLIENT)
    private IIcon iconOfMyBlock;

    public Block getBlock() {
        return block;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getSpriteNumber() {
        return block.getItemIconName() != null ? 1 : 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.iconOfMyBlock != null ? this.iconOfMyBlock : block.getIcon(1, meta);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        Block blockBeingUsedOn = world.getBlock(x, y, z);

        if (blockBeingUsedOn == Blocks.snow && (world.getBlockMetadata(x, y, z) & 7) < 1) {
            side = 1;
        } else if (blockBeingUsedOn != Blocks.vine && blockBeingUsedOn != Blocks.tallgrass && blockBeingUsedOn != Blocks.deadbush
                && (blockBeingUsedOn == null || !blockBeingUsedOn.isReplaceable(world, x, y, z))) {
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            x += dir.offsetX;
            y += dir.offsetY;
            z += dir.offsetZ;
        }

        if (stack.stackSize == 0) {
            return false;
        } else if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        } else if (y == 255 && block.getMaterial().isSolid()) {
            return false;
        } else if (world.canPlaceEntityOnSide(block, x, y, z, false, side, player, stack)) {
            int metadata = getMetadata(stack.getItemDamage());
            int k1 = block.onBlockPlaced(world, x, y, z, side, par8, par9, par10, metadata);

            if (placeBlockAt(stack, player, world, x, y, z, side, par8, par9, par10, k1)) {
                String placeSound = block.stepSound.func_150496_b();
                world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), placeSound, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                --stack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getUnlocalizedName() {
        return useItemName ? super.getUnlocalizedName() : block.getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTab() {
        return block.getCreativeTabToDisplayOn();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        block.getSubBlocks(par1, par2CreativeTabs, par3List);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        if (block.getItemIconName() != null) {
            this.iconOfMyBlock = par1IconRegister.registerIcon(block.getItemIconName());
        }
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, block, metadata, 3)) {
            return false;
        }

        if (world.getBlock(x, y, z) == block) {
            block.onBlockPlacedBy(world, x, y, z, player, stack);
            block.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }
}
