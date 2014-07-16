/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.utils.BitHelper;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.HashMap;
import java.util.Random;

public class BlockSink extends BlockJaffas {
    public static final int waterBit = 2;
    private static final boolean debug = false;
    public static final String LIQUID_WATER = "Water";

    public BlockSink(int texture) {
        super(texture, Material.iron);
        //setRequiresSelfNotify();
        setCreativeTab(null);
        setHardness(2f);
        setResistance(20f);
        //setTickRandomly(true);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileSink();
    }

    @Override
    public int getRenderType() {
        return ContentHolder.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setTileEntity(par2, par3, par4, this.createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && super.canPlaceBlockAt(par1World, par2, par3 + 1, par4);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isAirBlock(x, y + 1, z)) {
            BlockHelper.setAir(world, x, y, z);
            if (!world.isRemote) {
                this.dropBlockAsItem(world, x, y, z, 0, 0);
            }
        }
    }

    private static final HashMap<Item, Item> fillableItems;

    static {
        fillableItems = new HashMap<Item, Item>();
    }

    public static void addFillableItem(Item empty, Item full) {
        fillableItems.put(empty, full);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (debug && JaffasFood.debug && !world.isRemote) {
            int m = world.getBlockMetadata(x, y, z);
            PlayerHelper.addMessage(player, "meta: " + m);
        }

        if (player.isSneaking()) {
            return false;
        }

        int meta = world.getBlockMetadata(x, y, z);
        if (!isWaterReady(meta)) {
            return false;
        }

        ItemStack currentItem = player.getCurrentEquippedItem();
        if (currentItem != null) {
            Item filledItem = fillableItems.get(currentItem);
            if (filledItem != null) {
                changeStateToNoWater(world, x, y, z, meta);
                doItemSwap(world, player, currentItem, new ItemStack(filledItem, 1, 0));
                return true;
            } else {
                if (FluidContainerRegistry.isEmptyContainer(currentItem)) {
                    ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(FluidRegistry.getFluidStack(FluidRegistry.WATER.getName(), FluidContainerRegistry.BUCKET_VOLUME * 2), currentItem);
                    if (filledContainer != null) {
                        changeStateToNoWater(world, x, y, z, meta);
                        doItemSwap(world, player, currentItem, filledContainer);
                        return true;
                    } else {
                        // incompatible container?
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private void doItemSwap(World world, EntityPlayer player, ItemStack currentItem, ItemStack filledItem) {
        currentItem.stackSize--;
        PlayerHelper.giveItemToPlayer(player, filledItem);
    }

    private void changeStateToNoWater(World world, int x, int y, int z, int meta) {
        world.setBlockMetadataWithNotify(x, y, z, BitHelper.unsetBit(meta, waterBit), BlockHelper.NOTIFY_ALL());
    }

    public static boolean isWaterReady(int meta) {
        return BitHelper.isBitSet(meta, waterBit);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return JaffasFood.getItem(JaffaItem.sink);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(JaffasFood.getItem(JaffaItem.sink));
    }
}
