package monnef.jaffas.food.block

import monnef.core.block.BlockMonnefCore
import monnef.jaffas.food.JaffasFood
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import monnef.jaffas.food.common.IconDescriptorJaffas

trait BlockWithCustomName {
  def setUnlocalizedName(name: String): Block
}

trait BlockJaffasLike extends BlockWithCustomName with IconDescriptorJaffas {
  this: BlockMonnefCore =>

  setCreativeTab(JaffasFood.instance.creativeTab)

  abstract override def setUnlocalizedName(in: String): Block = super.setUnlocalizedName("jaffas." + in)
}

class BlockJaffas(_id: Int, _texture: Int, _material: Material) extends BlockMonnefCore(_id, _texture, _material) with BlockJaffasLike {
}

