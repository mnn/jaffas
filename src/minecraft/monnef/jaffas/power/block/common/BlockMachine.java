/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.api.IMachineTool;
import monnef.jaffas.power.api.IPipeWrench;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockMachine extends BlockPower {
    private boolean customRenderer;
    protected WrenchAction onWrench = WrenchAction.ROTATE;
    protected WrenchAction onWrenchSneaking = WrenchAction.DROP;
    protected int renderID;
    protected boolean useDefaultDirection = false;
    protected ForgeDirection defaultDirection = ForgeDirection.NORTH;

    public BlockMachine(int id, int index, Material material, boolean customRenderer) {
        super(id, index, material);
        this.customRenderer = customRenderer;
        if (useOwnRenderId()) {
            renderID = RenderingRegistry.getNextAvailableRenderId();
        }
    }

    public abstract TileEntity createTileEntity(World world, int meta);

    public TileEntityMachine getTile(IBlockAccess world, int x, int y, int z) {
        return (TileEntityMachine) world.getBlockTileEntity(x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLiving entity, ItemStack stack) {
        TileEntityMachine tile = getTile(w, x, y, z);

        if (useDefaultDirection) {
            MovingObjectPosition obj = entity.rayTrace(5, 1);
            if (obj != null && obj.typeOfHit == EnumMovingObjectType.TILE) {
                tile.setRotation(ForgeDirection.getOrientation(obj.sideHit).getOpposite());
            } else {
                tile.setRotation(defaultDirection);
                JaffasFood.Log.printDebug("No hit, using default side.");
            }
        } else {
            int direction = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            direction = (direction + 2) % 4; // rotation fix
            tile.setRotation(direction);
        }
    }

    public boolean useOwnRenderId() {
        return false;
    }

    @Override
    public int getRenderType() {
        if (customRenderer) {
            return useOwnRenderId() ? this.renderID : JaffasPower.renderID;
        } else {
            return super.getRenderType();
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return customRenderer ? false : super.renderAsNormalBlock();
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
        ItemStack hand = player.getCurrentEquippedItem();
        if (hand != null) {
            Item item = hand.getItem();
            if (isPipeWrenchOrCompatible(item)) {
                return this.onPipeWrenchClickDefault(world, x, y, z, player, side);
            } else if (item instanceof IMachineTool) {
                IMachineTool tool = (IMachineTool) item;
                TileEntityMachine machineTile = (TileEntityMachine) world.getBlockTileEntity(x, y, z);
                return tool.onMachineClick(machineTile, player, side);
            }
        }

        return false;
    }

    private boolean isPipeWrenchOrCompatible(Item item) {
        if (item instanceof IPipeWrench) return true;
        if (JaffasTechnic.omniWrenchId == item.itemID) return true;
        return false;
    }

    private boolean onPipeWrenchClickDefault(World world, int x, int y, int z, EntityPlayer player, int side) {
        WrenchAction action = player.isSneaking() ? onWrenchSneaking : onWrench;
        switch (action) {
            case DROP:
                if (!world.isRemote) {
                    world.setBlock(x, y, z, 0);
                    dropBlockAsItem(world, x, y, z, 0, 0); // meta, fortune
                }
                return true;

            case ROTATE:
                return doRotation(world, x, y, z, player, side);

            case CUSTOM:
                return onPipeWrenchClick(world, x, y, z, player, side);

            case NO_ACTION:
            default:
                return false;
        }
    }

    private boolean doRotation(World world, int x, int y, int z, EntityPlayer player, int side) {
        TileEntityMachine machine = (TileEntityMachine) world.getBlockTileEntity(x, y, z);
        if (machine instanceof TileEntityAntenna) {
            TileEntityAntenna ant = (TileEntityAntenna) machine;
            ant.changeRotation();
            return true;
        }
        return false;
    }

    protected boolean onPipeWrenchClick(World world, int x, int y, int z, EntityPlayer player, int side) {
        return false;
    }

    @Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null) {
            var7.receiveClientEvent(par5, par6);
        }
        return false;
    }

    public abstract boolean supportRotation();
}
