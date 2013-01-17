package monnef.jaffas.power.block.machine;

import monnef.jaffas.food.mod_jaffas;
import monnef.jaffas.power.api.IMachineTool;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class BlockMachine extends Block {
    private boolean customRenderer;

    public BlockMachine(int par1, int par2, Material par3Material, boolean customRenderer) {
        super(par1, par2, par3Material);
        this.customRenderer = customRenderer;
        setCreativeTab(mod_jaffas_power.CreativeTab);
    }

    @Override
    public String getTextureFile() {
        return mod_jaffas_power.textureFile;
    }

    public abstract TileEntityMachine createNewTileEntity(World world);

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLiving entity) {
        int var = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        var = (var + 2) % 4; // rotation fix
        w.setBlockMetadata(x, y, z, var);
    }

    @Override
    public int getRenderType() {
        return customRenderer ? mod_jaffas.renderID : super.getRenderType();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return customRenderer ? false : super.renderAsNormalBlock();
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setBlockTileEntity(par2, par3, par4, this.createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    public int getRotation(int meta) {
        return meta & 3;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
        ItemStack stack = par5EntityPlayer.getCurrentEquippedItem();
        if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof IMachineTool) {
                return ((IMachineTool) item).onMachineClick((TileEntityMachine) par1World.getBlockTileEntity(par2, par3, par4), par5EntityPlayer);
            }
        }

        return false;
    }
}
