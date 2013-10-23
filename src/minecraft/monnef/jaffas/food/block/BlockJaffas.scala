package monnef.jaffas.food.block

import monnef.core.block.BlockMonnefCore
import monnef.jaffas.food.JaffasFood
import net.minecraft.block.Block
import net.minecraft.block.material.Material


class BlockJaffas(_id: Int, _texture: Int, _material: Material) extends BlockMonnefCore(_id, _texture, _material) with BlockDescriptorJaffas {
  setCreativeTab(JaffasFood.instance.creativeTab)

  override def setUnlocalizedName(par1Str: String): Block = super.setUnlocalizedName("jaffas." + par1Str)
}
