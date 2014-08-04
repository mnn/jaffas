package monnef.jaffas.technic.network

import monnef.jaffas.food.network.JaffasPacket
import monnef.core.network.message.{MessageOut, MessageIn}
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.item.EntityItem
import monnef.jaffas.trees.block.TileFruitCollector

/**
 * Handles sync of [server] -> [client] sucking particles
 */
class FruitCollectorPacket extends JaffasPacket {

  var tileEntityCoordinates: (Int, Int, Int) = _
  var state: Byte = _
  var targetedItemPosition: (Double, Double, Double) = _

  def init(tile: TileEntity, state: Byte, entityItem: EntityItem) {
    tileEntityCoordinates = (tile.xCoord, tile.yCoord, tile.zCoord)
    this.state = state
    targetedItemPosition = (entityItem.posX, entityItem.posY, entityItem.posZ)
  }

  override def write(out: MessageOut[_]) {
    out.writeInt(tileEntityCoordinates._1)
    out.writeInt(tileEntityCoordinates._2)
    out.writeInt(tileEntityCoordinates._3)
    out.writeByte(state)
    out.writeDouble(targetedItemPosition._1)
    out.writeDouble(targetedItemPosition._2)
    out.writeDouble(targetedItemPosition._3)
  }

  override def read(in: MessageIn[_]) {
    tileEntityCoordinates = (in.readInt(), in.readInt(), in.readInt())
    state = in.readByte()
    targetedItemPosition = (in.readDouble(), in.readDouble(), in.readDouble())
  }

  override def executeClient(player: EntityPlayerSP) {
    super.executeClient(player)

    val collector = player.getEntityWorld.getTileEntity(tileEntityCoordinates._1, tileEntityCoordinates._2, tileEntityCoordinates._3).asInstanceOf[TileFruitCollector]
    collector.updateInnerState(state, targetedItemPosition._1, targetedItemPosition._2, targetedItemPosition._3)
  }
}
