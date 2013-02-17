package monnef.jaffas.food.block;

import monnef.core.BitHelper;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

import java.util.List;
import java.util.Random;

public class BlockSwitchgrass extends BlockJaffas implements IPlantable {
    private static final int BIT_TOP = 3;
    private static final int MAX_AGE = 7;
    private static final float border = 3f * 1f / 16f;
    private static final float borderComplement = 1f - border;

    public String[] subBlockNames;

    public BlockSwitchgrass(int id, int icon) {
        super(id, icon, Material.plants);
        this.setTickRandomly(true);
        subBlockNames = new String[16];
        for (int i = 0; i <= 15; i++) {
            subBlockNames[i] = "";
        }
        subBlockNames[15] = "Switchgrass";
        setBlockBounds(border, 0, border, borderComplement, 1, borderComplement);
    }

    // inspired by cactus
    public void updateTick(World world, int x, int y, int z, Random rand) {
        int myMeta = world.getBlockMetadata(x, y, z);
        if (isTop(myMeta) && world.isAirBlock(x, y + 1, z)) {
            int height = 1;
            while (world.getBlockId(x, y - height, z) == this.blockID) height++;
            int light = world.getBlockLightValue(x, y + 1, z);
            boolean onRain = world.canLightningStrikeAt(x, y + 1, z);

            if (height < 4 && (light > 5 || onRain) &&
                    (light > 9 || onRain || rand.nextBoolean())) {
                int age = getAge(myMeta);
                if (age < MAX_AGE) {
                    int newMeta = setAge(myMeta, age + 1 + (onRain ? 2 : 0));
                    world.setBlockMetadataWithNotify(x, y, z, newMeta);
                    //Log.printDebug(myMeta + " -> " + newMeta);
                } else {
                    world.setBlockAndMetadata(x, y + 1, z, this.blockID, BitHelper.setBit(0, BIT_TOP));
                    world.setBlockMetadataWithNotify(x, y, z, 0); // no longer the top one
                    world.markBlockForUpdate(x, y, z);
                }
            }
        }
    }

    private int setAge(int meta, int age) {
        if (age > MAX_AGE) {
            age = MAX_AGE;
        }

        return (isTop(meta) ? 8 : 0) | age;
    }

    public boolean isTop(int meta) {
        return BitHelper.isBitSet(meta, BIT_TOP);
    }

    public int getAge(int meta) {
        return meta & 7;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return mod_jaffas.renderSwitchgrassID;
    }

    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && this.floorCanSustainPlant(par1World, par2, par3, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        if (!this.canBlockStay(par1World, par2, par3, par4)) {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), par5);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
        int myMeta = par1World.getBlockMetadata(par2, par3, par4);
        int topBlock = par1World.getBlockId(par2, par3 + 1, par4);
        int bottomBlock = par1World.getBlockId(par2, par3 - 1, par4);

        boolean top = false;
        boolean bottom = false;

        if (isTop(myMeta)) {
            if (topBlock == 0) {
                top = true;
            }
        } else {
            if (topBlock == blockID) {
                top = true;
            }
        }

        if (bottomBlock == blockID || floorCanSustainPlant(par1World, par2, par3, par4)) {
            bottom = true;
        }

        return top && bottom;
    }

    public boolean floorCanSustainPlant(World par1World, int par2, int par3, int par4) {
        int var5 = par1World.getBlockId(par2, par3 - 1, par4);
        return blocksList[var5] != null && blocksList[var5].canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this);
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {
        return blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return -1;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, BitHelper.setBit(0, BIT_TOP)));
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
        return blockIndexInTexture + (isTop(par2) ? 0 : 1);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (mod_jaffas.debug) {
            int meta = par1World.getBlockMetadata(par2, par3, par4);
            if (par5EntityPlayer.isSneaking()) {
                par1World.setBlockMetadata(par2, par3, par4, MAX_AGE | (isTop(meta) ? 8 : 0));
            }
            par5EntityPlayer.addChatMessage("meta: " + meta);
        }

        return false;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        double rnd = par1Random.nextDouble();
        if (rnd < .2) {
            return 2;
        } else if (rnd < .7) {
            return 1;
        }
        return 0;
    }

    @Override
    public int damageDropped(int par1) {
        return 8;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }
}
