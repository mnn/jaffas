/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.jaffas.food.block.BlockJaffas;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.common.Reference;
import net.minecraft.block.material.Material;

public class BlockPower extends BlockJaffas {
    public BlockPower(int id, int index, Material material) {
        super(id, index, material);
        setCreativeTab(JaffasPower.CreativeTab);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 5;
    }
}
