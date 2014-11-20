package monnef.jaffas.food.common

import monnef.jaffas.food.entity.EntityLittleSpider
import net.minecraft.init.Blocks
import monnef.core.utils.RandomHelper

object CobWebInfluencingBlocksCalculator {
  val NEIGHBORHOOD_RADIUS = 5

  def calculateScoreTable(spider: EntityLittleSpider): Seq[(CobWebDescriptor, Int)] = {
    def roundInt(v: Double) = Math.round(v).asInstanceOf[Int]

    val sx = roundInt(spider.posX)
    val sy = roundInt(spider.posY)
    val sz = roundInt(spider.posZ)
    var res = Map(SpecialCobWebRegistry.getDescriptor(Blocks.web) -> NEIGHBORHOOD_RADIUS * NEIGHBORHOOD_RADIUS)
    for {
      x <- sx - NEIGHBORHOOD_RADIUS to sx + NEIGHBORHOOD_RADIUS
      y <- sy - NEIGHBORHOOD_RADIUS to sy + NEIGHBORHOOD_RADIUS
      z <- sz - NEIGHBORHOOD_RADIUS to sz + NEIGHBORHOOD_RADIUS
      currentBlock = spider.worldObj.getBlock(x, y, z)
      currentMeta = spider.worldObj.getBlockMetadata(x, y, z)
      (currentValue, desc) <- SpecialCobWebRegistry.getDescriptorInfluencedBy(currentBlock, currentMeta)
    } {
      if (!res.contains(desc)) res += desc -> 0
      res = res.updated(desc, res(desc) + currentValue)
    }
    res.toSeq
  }
}
