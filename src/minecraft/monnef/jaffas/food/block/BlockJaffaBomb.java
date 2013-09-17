/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.base.CustomIconHelper;
import monnef.jaffas.food.ContentHolder;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.item.JaffasHelper;
import monnef.jaffas.food.item.common.ItemManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

public class BlockJaffaBomb extends BlockJaffas {

    private float blastStrengh = 0.2F;
    private static Random rand = new Random();
    private static int itemCount = 15;
    private Icon specialTexture;

    public BlockJaffaBomb(int par1, int index, Material par3Material) {
        super(par1, index, par3Material);
        setCreativeTab(CreativeTabs.tabRedstone);
        setUnlocalizedName("Jaffa BOMB");
        setHardness(0.1F);
        setResistance(0.1F);
        this.setCreativeTab(JaffasFood.instance.creativeTab);
    }

    @Override
    public Icon getIcon(int par1, int par2) {
        return (par1 == 0 || par1 == 1) ? specialTexture : blockIcon;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
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
        w.createExplosion(null, x, y, z, blastStrengh, true);

        for (int i = 0; i < itemCount; i++) {
            int counter = 0;
            boolean notAir;
            double pX, pY, pZ;

            do {
                pX = x + rand.nextGaussian();
                pY = y + 1 + rand.nextGaussian() * 0.5;
                pZ = z + rand.nextGaussian();

                notAir = w.getBlockId((int) Math.floor(pX), (int) Math.floor(pY), (int) Math.floor(pZ)) != 0;
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
        if (w.getBlockId(x, y, z) == this.blockID) {
            this.detonate(w, x, y, z);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourId) {
        if (neighbourId > 0 && Block.blocksList[neighbourId].canProvidePower() && world.isBlockIndirectlyGettingPowered(x, y, z)) {
            detonate(world, x, y, z);
        }
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return 0;
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
        this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(ContentHolder.blockJaffaBomb));
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
        detonate(world, x, y, z);
    }
}
