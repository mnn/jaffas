/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.BlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockKitchenUnit extends BlockMachine {
    public BlockKitchenUnit(int id, int textureID) {
        super(id, textureID, Material.wood, false);
        setHardness(0.5f);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityKitchenUnit();
    }

    @Override
    public boolean supportRotation() {
        return false;
    }
}
