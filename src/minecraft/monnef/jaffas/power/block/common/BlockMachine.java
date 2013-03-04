package monnef.jaffas.power.block.common;

import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.food.mod_jaffas_food;
import monnef.jaffas.power.api.IMachineTool;
import monnef.jaffas.power.api.IPipeWrench;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.block.Block;
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
    protected WrenchAction onWrench = WrenchAction.DROP;
    protected int renderID;
    protected boolean useDefaultDirection = false;
    protected ForgeDirection defaultDirection = ForgeDirection.NORTH;

    public BlockMachine(int par1, int par2, Material par3Material, boolean customRenderer) {
        super(par1, par2, par3Material);
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
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLiving entity) {
        TileEntityMachine tile = getTile(w, x, y, z);

        if (useDefaultDirection) {
            MovingObjectPosition obj = entity.rayTrace(5, 1);
            if (obj != null && obj.typeOfHit == EnumMovingObjectType.TILE) {
                tile.setRotation(ForgeDirection.getOrientation(obj.sideHit).getOpposite());
            } else {
                tile.setRotation(defaultDirection);
                mod_jaffas_food.Log.printDebug("No hit, using default side.");
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
            return useOwnRenderId() ? this.renderID : mod_jaffas_power.renderID;
        } else {
            return super.getRenderType();
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return customRenderer ? false : super.renderAsNormalBlock();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        TileEntityMachine newTile = (TileEntityMachine) createTileEntity(world, world.getBlockMetadata(x, y, z));
        newTile.markForDirectConnectionTry();
        world.setBlockTileEntity(x, y, z, newTile);

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            int nx = x + dir.offsetX;
            int ny = y + dir.offsetY;
            int nz = z + dir.offsetZ;
            Block block = Block.blocksList[world.getBlockId(nx, ny, nz)];
            if (block instanceof BlockMachine) {
                TileEntity tile = world.getBlockTileEntity(nx, ny, nz);
                if (tile instanceof TileEntityMachine) {
                    // TODO more tests?
                    ((TileEntityMachine) tile).markForDirectConnectionTry();
                }
            }
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
        if (tile instanceof TileEntityMachine) {
            ((TileEntityMachine) tile).disconnectAll();
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
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
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        ItemStack stack = par5EntityPlayer.getCurrentEquippedItem();
        if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof IPipeWrench) {
                return this.onPipeWrenchClickDefault(par1World, par2, par3, par4, par5EntityPlayer, par6);
            } else if (item instanceof IMachineTool) {
                IMachineTool tool = (IMachineTool) item;
                TileEntityMachine machineTile = (TileEntityMachine) par1World.getBlockTileEntity(par2, par3, par4);
                return tool.onMachineClick(machineTile, par5EntityPlayer, par6);
            }
        }

        return false;
    }

    private boolean onPipeWrenchClickDefault(World world, int x, int y, int z, EntityPlayer player, int side) {
        switch (onWrench) {
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
    public void onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null) {
            var7.receiveClientEvent(par5, par6);
        }
    }

    public abstract boolean supportRotation();
}
