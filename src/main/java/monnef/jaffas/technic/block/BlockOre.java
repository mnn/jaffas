/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.RandomHelper;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import java.util.Random;

import static monnef.jaffas.technic.JaffasTechnic.blockJaffarrolOre;
import static monnef.jaffas.technic.JaffasTechnic.blockLimsewOre;

public class BlockOre extends BlockTechnic {

    public static final int NATURAL_META = 1;

    public BlockOre(int id, int textureID) {
        super(id, textureID, Material.rock);
        setStepSound(soundStoneFootstep);
        setHardness(3);
        setResistance(5);
    }

    @Override
    public int quantityDropped(Random par1Random) {
        if (blockID == blockLimsewOre.blockID) {
            return 1 + (par1Random.nextDouble() > 0.8 ? 1 : 0);
        }

        return super.quantityDropped(par1Random);
    }

    @Override
    public int idDropped(int id, Random random, int fortune) {
        if (blockID == blockLimsewOre.blockID) {
            return JaffasTechnic.limsew.itemID;
        }
        return super.idDropped(id, random, fortune);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        super.dropBlockAsItemWithChance(world, x, y, z, meta, chance, fortune);

        int xpAmount = 0;

        if (blockID == blockLimsewOre.blockID) {
            xpAmount = RandomHelper.generateRandomFromInterval(4, 10);
        } else if (blockID == blockJaffarrolOre.blockID && meta == NATURAL_META) {
            xpAmount = RandomHelper.generateRandomFromInterval(1, 4);
        }

        this.dropXpOnBlockBreak(world, x, y, z, xpAmount);
    }
}
