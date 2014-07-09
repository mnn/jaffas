package monnef.jaffas.food.common

import monnef.core.utils.BiomeHelper
import net.minecraftforge.common.BiomeDictionary.Type

import scala.collection.JavaConverters._
import monnef.jaffas.food.block.BlockSwitchgrass

object SwitchgrassBiomeRegistrar {
  def register() {
    // MinecraftForge.addGrassPlant(blockSwitchgrass, BlockSwitchgrass.VALUE_TOP, 5);
    BiomeHelper.compileListOr(Type.FOREST, Type.PLAINS).asScala.foreach(
      _.addFlower(ContentHolder.blockSwitchgrass, BlockSwitchgrass.VALUE_TOP, 5)
    )
  }
}
