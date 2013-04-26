/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.trees.block;

import monnef.core.base.CustomIconHelper;
import monnef.core.utils.InventoryUtils;
import monnef.jaffas.food.block.BlockContainerJaffas;
import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.common.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.Random;

public class BlockFruitCollector extends BlockContainerJaffas {
    private Icon topIcon;

    public BlockFruitCollector(int id) {
        super(id, 95, Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setUnlocalizedName("blockFruitCollector");
        setCreativeTab(JaffasTrees.CreativeTab);
        setSheetNumber(1);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int idk, float what, float these, float are) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }

        player.openGui(JaffasTrees.instance, 0, world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        super.breakBlock(world, x, y, z, par5, par6);
        InventoryUtils.dropItems(world, x, y, z);
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        spawnParticlesOfTargetedItem(world, random, i, j, k, false);

        /*
        for (int a = 0; a < 3; a++) {
            JaffasTrees.proxy.addEffect("sucking", world, i + randomShift(random), j + randomShift(random), k + randomShift(random), 0D, 0.02D,0D);
        }
        */
    }

    public void spawnParticlesOfTargetedItem(World world, Random random, int i, int j, int k, boolean force) {
        TileEntityFruitCollector et = (TileEntityFruitCollector) world.getBlockTileEntity(i, j, k);
        if (et.getState() == TileEntityFruitCollector.CollectorStates.targeted || force) {
            for (int a = 0; a < 15; a++) {
                JaffasTrees.proxy.addEffect("sucking", world, et.getIX() + randomShift(random), et.getIY() + randomShift(random), et.getIZ() + randomShift(random), (random.nextDouble() - 0.5) / 40D, (random.nextDouble()) / 200D, (random.nextDouble() - 0.5) / 40D);
            }

        }
    }

    private double randomShift(Random random) {
        return random.nextDouble() % 1D - 0.5D;
    }


    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityFruitCollector();
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return getTextureFromSide(side);
    }

    private Icon getTextureFromSide(int side) {
        ForgeDirection s = ForgeDirection.getOrientation(side);
        if (s == ForgeDirection.DOWN || s == ForgeDirection.UP) {
            return topIcon;
        } else {
            return blockIcon;
        }
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
        topIcon = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, 1));
    }
}