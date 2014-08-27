package monnef.jaffas.food.common

import net.minecraft.item.{ItemStack, Item}
import net.minecraft.block.Block
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.event.entity.player.FillBucketEvent
import net.minecraft.world.World
import net.minecraft.util.MovingObjectPosition
import monnef.core.utils.BlockHelper
import cpw.mods.fml.common.eventhandler.Event.Result
import monnef.jaffas.food.block.BlockJaffaFiniteFluid

object BucketHandler {
  private[this] var blockFluidToBucketItem: Map[BlockJaffaFiniteFluid, Item] = Map()

  @SubscribeEvent
  def onBucketFill(event: FillBucketEvent) {
    fillBucket(event.world, event.target) match {
      case null =>
      case result =>
        event.result = result
        event.setResult(Result.ALLOW)
    }
  }

  private def fillBucket(world: World, pos: MovingObjectPosition): ItemStack = {
    val (x, y, z) = (pos.blockX, pos.blockY, pos.blockZ)
    world.getBlock(x, y, z) match {
      case block: BlockJaffaFiniteFluid =>
        blockFluidToBucketItem.get(block) match {
          case Some(bucket) if block.isFullyFilled(world, x, y, z) =>
            BlockHelper.setAir(world, x, y, z)
            new ItemStack(bucket)
          case None => null
        }
      case _ => null
    }
  }

  def registerBucket(liquid: BlockJaffaFiniteFluid, bucket: Item) {
    blockFluidToBucketItem += liquid -> bucket
  }
}
