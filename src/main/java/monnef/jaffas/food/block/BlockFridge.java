/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.utils.InventoryUtils;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.client.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static monnef.core.common.CustomIconHelper.generateId;

public class BlockFridge extends BlockContainerJaffas {
    private static IIcon FridgeBottom;
    private static IIcon FridgeTopTop;
    private static IIcon FridgeTopBottom;
    private static IIcon FridgeLeft;
    private static IIcon FridgeRight;
    private static IIcon FridgeFront;
    private static IIcon FridgeBack;

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        FridgeBottom = iconRegister.registerIcon(generateId(this, 43));
        FridgeTopTop = iconRegister.registerIcon(generateId(this, 40));
        FridgeTopBottom = iconRegister.registerIcon(generateId(this, 41));
        FridgeLeft = iconRegister.registerIcon(generateId(this, 38));
        FridgeRight = iconRegister.registerIcon(generateId(this, 39));
        FridgeFront = iconRegister.registerIcon(generateId(this, 37));
        FridgeBack = iconRegister.registerIcon(generateId(this, 42));
    }

    public BlockFridge() {
        super(0, Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setBlockName("blockFridge");
        // setRequiresSelfNotify();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int idk, float what, float these, float are) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }

        player.openGui(JaffasFood.instance, GuiHandler.GuiTypes.FRIDGE.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        InventoryUtils.dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFridge();
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        int front;

        front = access.getBlockMetadata(x, y, z);

        int back, left = 0, right = 0;
        switch (front) {
            case 3:
                back = 2;
                left = 4;
                right = 5;
                break;
            case 2:
                back = 3;
                left = 5;
                right = 4;
                break;
            case 4:
                back = 5;
                left = 2;
                right = 3;
                break;
            case 5:
                back = 4;
                left = 3;
                right = 2;
                break;
            default:
                back = 0;
        }

        switch (side) {
            case 0:
                return FridgeBottom;
            case 1:
                switch (front) {
                    case 3:
                        return FridgeTopBottom;
                    case 2:
                        return FridgeTopTop;
                    case 4:
                        return FridgeRight;
                    case 5:
                        return FridgeLeft;
                    default:
                        return FridgeBottom;
                }
            default:
                if (side == front) {
                    return FridgeFront;
                } else if (side == back) {
                    return FridgeBack;
                } else if (side == left) {
                    return FridgeLeft;
                } else if (side == right) {
                    return FridgeRight;
                } else {
                    return FridgeBottom;
                }
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return getTextureFromSide(side);
    }

    private IIcon getTextureFromSide(int side) {
        switch (side) {
            case 0:
                return FridgeBottom;
            case 1:
                return FridgeTopBottom;
            case 2:
                return FridgeBack;
            case 3:
                return FridgeFront;
            case 4:
                return FridgeLeft;
            case 5:
                return FridgeRight;
            default:
                return null;
        }
    }

    /**
     * set a blocks direction
     */
    private void setDefaultDirection(World par1World, int par2, int par3, int par4) {
        TileEntity blockEntity = par1World.getTileEntity(par2, par3, par4);
        if (par1World.isRemote) {
            return;
        }

        Block i = par1World.getBlock(par2, par3, par4 - 1);
        Block j = par1World.getBlock(par2, par3, par4 + 1);
        Block k = par1World.getBlock(par2 - 1, par3, par4);
        Block l = par1World.getBlock(par2 + 1, par3, par4);
        byte byte0 = 3;

        if (i.isOpaqueCube() && !j.isOpaqueCube()) {
            byte0 = 3;
        }
        if (j.isOpaqueCube() && !i.isOpaqueCube()) {
            byte0 = 2;
        }
        if (k.isOpaqueCube() && !l.isOpaqueCube()) {
            byte0 = 5;
        }
        if (l.isOpaqueCube() && !k.isOpaqueCube()) {
            byte0 = 4;
        }

        ((TileFridge) blockEntity).setFront(byte0);
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        setDefaultDirection(par1World, par2, par3, par4);
    }

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        int var = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        TileEntity blockEntity = w.getTileEntity(x, y, z);
        int front = 0;

        switch (var) {
            case 0:
                front = 2;
                break;

            case 1:
                front = 5;
                break;

            case 2:
                front = 3;
                break;

            case 3:
                front = 4;
                break;

        }

        ((TileFridge) blockEntity).setFront(front);
        w.setBlockMetadataWithNotify(x, y, z, front, 2 + 4);

        w.notifyBlockChange(x, y, z, this);
        w.notifyBlocksOfNeighborChange(x, y, z, this);
    }
}