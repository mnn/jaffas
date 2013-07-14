/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.BoundingBoxSize;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.api.IMachineTool;
import monnef.jaffas.power.common.WrenchHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockMachine extends BlockPower {
    public static final int ROTATIONS_COUNT = 4;
    private boolean customRenderer;
    protected WrenchAction onWrench = WrenchAction.ROTATE;
    protected WrenchAction onWrenchSneaking = WrenchAction.DROP;
    protected int renderID;
    protected boolean useDefaultDirection = false;
    protected ForgeDirection defaultDirection = ForgeDirection.NORTH;
    private boolean useOwnRenderId;
    protected int rotationShiftInPlacing = 2;
    private boolean useRotatedBoundingBox = false;
    private BoundingBoxSize customBoundingBox;

    public BlockMachine(int id, int index, Material material, boolean customRenderer, boolean useOwnRenderingId) {
        super(id, index, material);
        this.customRenderer = customRenderer;
        this.useOwnRenderId = useOwnRenderingId;
        if (getUseOwnRenderId()) {
            renderID = RenderingRegistry.getNextAvailableRenderId();
        }
    }

    public abstract TileEntity createTileEntity(World world, int meta);

    public TileEntityMachine getTile(IBlockAccess world, int x, int y, int z) {
        return (TileEntityMachine) world.getBlockTileEntity(x, y, z);
    }

    public void setRotationShiftInPlacing(int rotationShiftInPlacing) {
        this.rotationShiftInPlacing = rotationShiftInPlacing % ROTATIONS_COUNT;
    }

    public void setCustomRotationSensitiveBoundingBox(float x1, float y1, float z1, float x2, float y2, float z2) {
        customBoundingBox = new BoundingBoxSize(x1, x2, y1, y2, z1, z2);
        useRotatedBoundingBox = true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        if (!useRotatedBoundingBox) return;
        setBlockBounds(BlockHelper.rotateBoundingBoxCoordinates(customBoundingBox, getTile(world, x, y, z).getRotation().ordinal(), x, y, z, false));
    }

    public void setBlockBounds(AxisAlignedBB bb) {
        setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
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
            direction = (direction + rotationShiftInPlacing) % ROTATIONS_COUNT; // rotation fix
            tile.setRotation(direction);
        }

        tile.markRedstoneStatusDirty();
    }

    public boolean getUseOwnRenderId() {
        return useOwnRenderId;
    }

    @Override
    public int getRenderType() {
        if (customRenderer) {
            return getUseOwnRenderId() ? this.renderID : JaffasPower.renderID;
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
            if (WrenchHelper.isPipeWrenchOrCompatible(item)) {
                return this.onPipeWrenchClickDefault(world, x, y, z, player, side);
            } else if (item instanceof IMachineTool) {
                IMachineTool tool = (IMachineTool) item;
                TileEntityMachine machineTile = (TileEntityMachine) world.getBlockTileEntity(x, y, z);
                return tool.onMachineClick(machineTile, player, side);
            }
        }

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
        boolean res = machine.toggleRotation();
        machine.sendUpdate();
        return res;
    }

    protected boolean onPipeWrenchClick(World world, int x, int y, int z, EntityPlayer player, int side) {
        return false;
    }

    // ?
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

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourId) {
        super.onNeighborBlockChange(world, x, y, z, neighbourId);
        getTile(world, x, y, z).markRedstoneStatusDirty();
    }
}
