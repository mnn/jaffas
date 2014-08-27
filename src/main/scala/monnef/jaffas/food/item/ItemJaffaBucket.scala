package monnef.jaffas.food.item

import net.minecraft.item.ItemBucket
import net.minecraft.block.Block
import monnef.core.block.GameObjectDescriptor
import monnef.jaffas.food.common.IconDescriptorJaffas
import monnef.core.item.CustomItemIconTrait
import net.minecraft.world.World
import monnef.core.utils.BlockHelper
import monnef.jaffas.food.block.BlockJaffaFiniteFluid

class ItemJaffaBucket(liquidBlock: BlockJaffaFiniteFluid) extends ItemBucket(liquidBlock) with GameObjectDescriptor with IconDescriptorJaffas with CustomItemIconTrait {
  if (liquidBlock == null) {
    throw new RuntimeException("Invalid block - null.")
  }

  override def tryPlaceContainedLiquid(world: World, x: Int, y: Int, z: Int): Boolean = {
    if (super.tryPlaceContainedLiquid(world, x, y, z)) {
      if (world.getBlock(x, y, z) == liquidBlock) BlockHelper.setBlockMetadata(world, x, y, z, liquidBlock.getMaxMeta())
      true
    } else false
  }
}
