package monnef.jaffas.technic.block;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

import static monnef.jaffas.technic.JaffasTechnic.blockLimsewOre;

public class BlockOre extends BlockTechnic {
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
    public int idDropped(int par1, Random par2Random, int par3) {
        if (blockID == blockLimsewOre.blockID) {
            return JaffasTechnic.limsew.itemID;
        }
        return super.idDropped(par1, par2Random, par3);
    }

    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

        if (this.idDropped(par5, par1World.rand, par7) != this.blockID) {
            int var8 = 0;

            if (this.blockID == blockLimsewOre.blockID) {
                var8 = MathHelper.getRandomIntegerInRange(par1World.rand, 4, 10);
            }

            this.dropXpOnBlockBreak(par1World, par2, par3, par4, var8);
        }
    }
}
