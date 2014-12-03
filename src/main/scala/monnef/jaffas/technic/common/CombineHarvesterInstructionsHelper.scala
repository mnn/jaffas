package monnef.jaffas.technic.common

import monnef.jaffas.technic.entity.EntityCombineHarvester
import monnef.jaffas.technic.entity.EntityCombineHarvester.{JaffaCropHarvestingInstruction, MetaReplantBlockHarvestingInstruction, SimpleDestroyBlockHarvestingInstruction}
import monnef.jaffas.trees.{BushManager, JaffasTrees}
import net.minecraft.block.Block

object CombineHarvesterInstructionsHelper {
  def registerAll() {
    registerVanillaBlocks()
    registerJaffaCrops()
  }

  private def registerDestroyInstruction(b: Block) {
    EntityCombineHarvester.registerBlockHarvestingInstruction(b, new SimpleDestroyBlockHarvestingInstruction)
  }

  private def registerMetaDegradationInstruction(b: Block, grownMeta: Int) {
    EntityCombineHarvester.registerBlockHarvestingInstruction(b, new MetaReplantBlockHarvestingInstruction(Set(grownMeta), 0))
  }

  private def registerJaffaCropHarvestingInstruction(b: Block) {
    EntityCombineHarvester.registerBlockHarvestingInstruction(b, new JaffaCropHarvestingInstruction)
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
