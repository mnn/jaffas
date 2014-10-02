package monnef.jaffas.trees.compatibility

import mcp.mobius.waila.api.{IWailaRegistrar, IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.ItemStack
import java.util
import monnef.jaffas.trees.block.BlockJaffaCrops

class WailaCrops extends IWailaDataProvider {

  import WailaCrops._

  override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler): ItemStack = null

  override def getWailaHead(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler): util.List[String] = currenttip

  override def getWailaBody(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler): util.List[String] = {
    accessor.getBlock match {
      case cropBlock: BlockJaffaCrops => currenttip.add(generateGrowthStatus(cropBlock, accessor.getMetadata))
      case _ =>
    }
    currenttip
  }

  def generateGrowthStatus(crop: BlockJaffaCrops, meta: Int): String = GROWTH_PRE + (if (crop.isMature(meta)) GROWTH_MATURE else crop.generateGrowthStatusString(meta))

  override def getWailaTail(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler): util.List[String] = currenttip
}

object WailaCrops {
  final val GROWTH_PRE = "Growth: "
  final val GROWTH_MATURE = "mature"

  def callbackRegister(registrar: IWailaRegistrar) {
    registrar.registerBodyProvider(new WailaCrops, classOf[BlockJaffaCrops])
  }
}
