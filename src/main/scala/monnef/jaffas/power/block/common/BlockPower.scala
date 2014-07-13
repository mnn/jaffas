package monnef.jaffas.power.block.common

import monnef.jaffas.food.block.BlockJaffas
import monnef.jaffas.power.JaffasPower
import net.minecraft.block.material.Material
import monnef.jaffas.power.common.IconDescriptorPower

class BlockPower(_id: Int, _tex: Int, _mat: Material) extends BlockJaffas(_id, _tex, _mat) with IconDescriptorPower {
  setCreativeTab(JaffasPower.instance.creativeTab)
}