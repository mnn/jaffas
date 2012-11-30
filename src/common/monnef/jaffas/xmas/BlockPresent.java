package monnef.jaffas.xmas;

import net.minecraft.src.*;

public class BlockPresent extends BlockXmas {
    public final static float unit = 1f / 16f;
    public final static float f2 = unit * 2;
    public final static float f2d = 1f - unit * 2f;

    public BlockPresent(int id, int textureID, Material material) {
        super(id, textureID, material);
        this.setBlockBounds(f2, 0f, f2, f2d, 1 - 1f / 16 * 7, f2d);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityPresent();
    }

    @Override
    public int getRenderType() {
        return mod_jaffas_xmas.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
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

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (par1World.isRemote) {
            return true;
        }

        TileEntityPresent te = (TileEntityPresent) par1World.getBlockTileEntity(par2, par3, par4);

        if (te.getContent() == null) {
            ItemStack equippedItem = player.getCurrentEquippedItem();
            if (equippedItem != null) {
                te.setContent(equippedItem.copy());
                player.destroyCurrentEquippedItem();
            }
        } else {
            ItemStack item = te.getContent();
            te.setContent(null);
            EntityItem ei = new EntityItem(par1World, player.posX, player.posY + 1, player.posZ, item);
            par1World.spawnEntityInWorld(ei);
        }

        return true;
    }
}
