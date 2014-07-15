package monnef.jaffas.technic.block

import monnef.jaffas.food.block.BlockJaffas
import monnef.jaffas.technic.JaffasTechnic
import net.minecraft.block.material.Material
import monnef.jaffas.technic.common.IconDescriptorTechnic

class BlockTechnic(_tex: Int, _mat: Material) extends BlockJaffas(_tex, _mat) with IconDescriptorTechnic {
  setCreativeTab(JaffasTechnic.instance.creativeTab)
}