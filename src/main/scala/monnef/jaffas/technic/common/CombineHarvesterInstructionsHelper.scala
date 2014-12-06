package monnef.jaffas.technic.common

import monnef.jaffas.technic.common.CombineHarvesterRegistry.{JaffaCropHarvestingInstruction, MetaReplantBlockHarvestingInstruction, SimpleDestroyBlockHarvestingInstruction}
import monnef.jaffas.technic.entity.EntityCombineHarvester
import monnef.jaffas.trees.BushManager
import net.minecraft.block.Block

object CombineHarvesterInstructionsHelper {
  def registerAll() {
    registerVanillaBlocks()
    registerJaffaCrops()
  }

  private def registerDestroyInstruction(b: Block) {
    CombineHarvesterRegistry.registerBlockHarvestingInstruction(b, new SimpleDestroyBlockHarvestingInstruction)
  }

  private def registerMetaDegradationInstruction(b: Block, grownMeta: Int) {
    CombineHarvesterRegistry.registerBlockHarvestingInstruction(b, new MetaReplantBlockHarvestingInstruction(Set(grownMeta), 0))
  }

  private def registerJaffaCropHarvestingInstruction(b: Block) {
    CombineHarvesterRegistry.registerBlockHarvestingInstruction(b, new JaffaCropHarvestingInstruction)
  }

  private def registerVanillaBlocks() {
    import net.minecraft.init.Blocks._

    Seq(red_flower, yellow_flower, tallgrass, deadbush, brown_mushroom, red_mushroom, cactus, reeds, pumpkin, melon_block, cocoa).
      foreach(registerDestroyInstruction)

    Seq(wheat, nether_wart, carrots, potatoes).
      foreach(registerMetaDegradationInstruction(_, 7))
  }

  private def registerJaffaCrops() {
    import scala.collection.JavaConverters._
    BushManager.bushesList.values().asScala.map(_.block).foreach(registerJaffaCropHarvestingInstruction)
  }
}
