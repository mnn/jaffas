/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.base.CustomIconHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
        setUnlocalizedName("Jaffa Cakes BOMB");
        setHardness(0.1F);
        setResistance(0.1F);
        this.setCreativeTab(JaffasFood.instance.CreativeTab);
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    @Override
    public Icon getIcon(int par1, int par2) {
        return (par1 == 0 || par1 == 1) ? specialTexture : blockIcon;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
        specialTexture = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, 1));
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);

        if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
            detonate(par1World, par2, par3, par4);
        }
    }

    private void detonate(World w, int par2, int par3, int par4) {
        w.createExplosion((Entity) null, par2, par3, par4, blastStrengh, true);

        for (int i = 0; i < itemCount; i++) {
            int counter = 0;
            boolean notAir;
            double pX, pY, pZ;

            do {
                pX = par2 + rand.nextGaussian();
                pY = par3 + 1 + rand.nextGaussian() * 0.5;
                pZ = par4 + rand.nextGaussian();

                notAir = w.getBlockId((int) Math.floor(pX), (int) Math.floor(pY), (int) Math.floor(pZ)) != 0;
                counter++;
            } while (notAir && counter < 5);

            ItemStack item;

            JaffaItem jaffaItem;
            if (rand.nextDouble() > 0.5) {
                jaffaItem = JaffaItem.jaffa;
            } else {
                if (rand.nextDouble() > 0.5) {
                    jaffaItem = JaffaItem.jaffaO;
                } else {
                    jaffaItem = JaffaItem.jaffaR;
                }
            }

            if (!w.isRemote) {
                item = new ItemStack(ItemManager.getItem(jaffaItem));

                EntityItem entity = new EntityItem(w, pX, pY, pZ, item);
                entity.addVelocity(rand.nextGaussian() * 0.5, 0.1 + rand.nextDouble() * 1.5, rand.nextGaussian() * 0.5);
                w.spawnEntityInWorld(entity);
            }
        }

        w.setBlock(par2, par3, par4, 0);

        testNeighbourForBomb(w, par2 + 1, par3, par4);
        testNeighbourForBomb(w, par2 - 1, par3, par4);
        testNeighbourForBomb(w, par2, par3 + 1, par4);
        testNeighbourForBomb(w, par2, par3 - 1, par4);
        testNeighbourForBomb(w, par2, par3, par4 + 1);
        testNeighbourForBomb(w, par2, par3, par4 - 1);
    }

    private void testNeighbourForBomb(World w, int x, int y, int z) {
        if (w.getBlockId(x, y, z) == this.blockID) {
            this.detonate(w, x, y, z);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        if (par5 > 0 && Block.blocksList[par5].canProvidePower() && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
            detonate(par1World, par2, par3, par4);
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
        this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(JaffasFood.blockJaffaBomb));
    }

    @Override
    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion) {
        detonate(par1World, par2, par3, par4);
    }
}
