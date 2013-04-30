/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.block;

import monnef.core.base.CustomIconHelper;
import monnef.core.utils.BitHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;

import java.util.List;
import java.util.Random;

import static monnef.core.utils.BlockHelper.setBlock;
import static monnef.core.utils.BlockHelper.setBlockMetadata;
import static monnef.core.utils.BlockHelper.setBlockWithoutNotify;

public class BlockSwitchgrass extends BlockJaffas implements IPlantable {
    private static final int BIT_TOP = 3;
    private static final int MAX_AGE = 7;
    private static final float border = 3f * 1f / 16f;
    private static final float borderComplement = 1f - border;
    public static int maximalHeight = 4;
    private static Icon bodyIcon;
    public static final int VALUE_TOP = BitHelper.setBit(0, BIT_TOP);

    public String[] subBlockNames;

    public BlockSwitchgrass(int id, int icon) {
        super(id, icon, Material.plants);
        this.setTickRandomly(true);
        subBlockNames = new String[16];
        for (int i = 0; i < subBlockNames.length; i++) {
            subBlockNames[i] = "Switchgrass";
        }
        setBlockBounds(border, 0, border, borderComplement, 1, borderComplement);
        setUnlocalizedName("blockJSwitchgrass");
    }

    // inspired by cactus
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isRemote) return;
        tryGrow(world, x, y, z, rand);
    }

    private void tryGrow(World world, int x, int y, int z, Random rand) {
        if (world.isRemote) return;
        int myMeta = world.getBlockMetadata(x, y, z);
        if (isTop(myMeta) && world.isAirBlock(x, y + 1, z)) {
            int height = 1;
            while (world.getBlockId(x, y - height, z) == this.blockID) height++;
            int light = world.getBlockLightValue(x, y + 1, z);
            boolean onRain = world.canLightningStrikeAt(x, y + 1, z);

            if (height < maximalHeight && (light > 5 || onRain) &&
                    (light > 9 || onRain || rand.nextBoolean())) {
                int age = getAge(myMeta);
                if (age < MAX_AGE) {
                    int newMeta = setAge(myMeta, age + 1 + (onRain ? 2 : 0));
                    setBlockMetadata(world, x, y, z, newMeta);
                    //Log.printDebug(myMeta + " -> " + newMeta);
                } else {
                    setBlockWithoutNotify(world, x, y + 1, z, this.blockID, VALUE_TOP);
                    setBlockMetadata(world, x, y, z, 0); // no longer the top one
                    world.markBlockForUpdate(x, y + 1, z);
                }
            }
        }
    }

    private int setAge(int meta, int age) {
        if (age > MAX_AGE) {
            age = MAX_AGE;
        }

        //return (isTop(meta) ? 8 : 0) | age;
        return BitHelper.setBitToValue(age, BIT_TOP, isTop(meta));
    }

    public boolean isTop(int meta) {
        return BitHelper.isBitSet(meta, BIT_TOP);
    }

    public int getAge(int meta) {
        return BitHelper.unsetBit(meta, BIT_TOP);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return JaffasFood.renderSwitchgrassID;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && this.floorCanSustainPlant(par1World, par2, par3, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        if (!this.canBlockStay(par1World, par2, par3, par4)) {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), par5);
            setBlock(par1World, par2, par3, par4, 0);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
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
        par3List.add(new ItemStack(this, 1, VALUE_TOP));
    }

    @Override
    public Icon getIcon(int par1, int par2) {
        return isTop(par2) ? blockIcon : bodyIcon;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (JaffasFood.debug) {
            int meta = par1World.getBlockMetadata(par2, par3, par4);
            if (par5EntityPlayer.isSneaking()) {
                setBlockMetadata(par1World, par2, par3, par4, MAX_AGE | (isTop(meta) ? 8 : 0));
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
        return VALUE_TOP;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
        bodyIcon = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, 1));
    }

    @ForgeSubscribe
    public void onBonemeal(BonemealEvent event) {
        if (event.ID != blockID) {
            return;
        }

        if (tryBonemeal(event.world, event.X, event.Y, event.Z)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    public boolean tryBonemeal(World w, int x, int y, int z) {
        if (JaffasTrees.bonemealingAllowed) {
            if (!w.isRemote) {
                int higherY = y;
                while (!isTop(w.getBlockMetadata(x, higherY, z)) && w.getBlockId(x, higherY, z) == blockID) {
                    higherY++;
                    if (higherY - y > maximalHeight) return false;
                }
                int lowerY = y;
                while (w.getBlockId(x, lowerY, z) == blockID) {
                    lowerY--;
                    if (higherY - lowerY > maximalHeight) return false;
                }
                lowerY++; // fix to point on a lowest grass block, not on a soil
                if (higherY - lowerY + 1 >= maximalHeight) return false;
                if (JaffasFood.rand.nextFloat() < 1.0) {
                    tryGrow(w, x, higherY, z, JaffasFood.rand);
                }
                w.playAuxSFX(2005, x, higherY, z, 0);
                return true;
            }
        }
        return false;
    }
}
