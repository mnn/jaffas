/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.common.CustomIconHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.item.JaffasHelper;
import monnef.jaffas.food.item.common.ItemManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

public class BlockJaffaBomb extends BlockJaffas {

    private float blastStrength = 0.2F;
    private static Random rand = new Random();
    private static int itemCount = 15;
    private IIcon specialTexture;

    public BlockJaffaBomb(int index, Material par3Material) {
        super(index, par3Material);
        setCreativeTab(CreativeTabs.tabRedstone);
        setBlockName("jaffaBomb");
        setHardness(0.1F);
        setResistance(0.1F);
        this.setCreativeTab(JaffasFood.instance.creativeTab);
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return (par1 == 0 || par1 == 1) ? specialTexture : blockIcon;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerBlockIcons(iconRegister);
        specialTexture = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, 1));
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);

        if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
            detonate(par1World, par2, par3, par4);
        }
    }

    private void detonate(World w, int x, int y, int z) {
        w.setBlockToAir(x, y, z);
        w.createExplosion(null, x, y, z, blastStrength, true);

        for (int i = 0; i < itemCount; i++) {
            int counter = 0;
            boolean notAir;
            double pX, pY, pZ;

            do {
                pX = x + rand.nextGaussian();
                pY = y + 1 + rand.nextGaussian() * 0.5;
                pZ = z + rand.nextGaussian();

                notAir = !w.isAirBlock((int) Math.floor(pX), (int) Math.floor(pY), (int) Math.floor(pZ));
                counter++;
            } while (notAir && counter < 5);

            ItemStack item;

            if (!w.isRemote) {
                item = new ItemStack(ItemManager.getItem(JaffasHelper.getRandomJaffa()));

                EntityItem entity = new EntityItem(w, pX, pY, pZ, item);
                entity.addVelocity(rand.nextGaussian() * 0.5, 0.1 + rand.nextDouble() * 1.5, rand.nextGaussian() * 0.5);
                w.spawnEntityInWorld(entity);
            }
        }

        testAllNeighboursForBomb(w, x, y, z);
    }

    private void testAllNeighboursForBomb(World w, int x, int y, int z) {
        testNeighbourForBomb(w, x + 1, y, z);
        testNeighbourForBomb(w, x - 1, y, z);
        testNeighbourForBomb(w, x, y + 1, z);
        testNeighbourForBomb(w, x, y - 1, z);
        testNeighbourForBomb(w, x, y, z + 1);
        testNeighbourForBomb(w, x, y, z - 1);
    }

    private void testNeighbourForBomb(World w, int x, int y, int z) {
        if (w.getBlock(x, y, z) == this) {
            this.detonate(w, x, y, z);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbour) {
        if (neighbour.canProvidePower() && world.isBlockIndirectlyGettingPowered(x, y, z)) {
            detonate(world, x, y, z);
        }
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemById(0);
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
        dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(ContentHolder.blockJaffaBomb));
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
        detonate(world, x, y, z);
    }
}
