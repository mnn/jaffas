package monnef.jaffas.xmas.block

import monnef.jaffas.food.block.BlockJaffas
import monnef.jaffas.xmas.common.IconDescriptorXmas
import net.minecraft.block.material.Material
import monnef.jaffas.xmas.JaffasXmas

class BlockXmas(_texture: Int, _mat: Material) extends BlockJaffas(_texture, _mat) with IconDescriptorXmas {
  setCreativeTab(JaffasXmas.instance.creativeTab)
}
