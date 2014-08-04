package monnef.jaffas.food.network

import monnef.core.network.message.{MessageOut, MessageIn}
import monnef.jaffas.food.network.HomeStonePacketType.HomeStonePacketType
import com.ibm.icu.impl.InvalidFormatException
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import monnef.jaffas.food.JaffasFood
import monnef.jaffas.food.common.CoolDownRegistry
import monnef.jaffas.food.common.CoolDownType._
import monnef.jaffas.food.JaffasFood._
import monnef.jaffas.food.item.ItemSpawnStone
import monnef.core.utils.PlayerHelper

class HomeStonePacket extends JaffasPacket {

  import HomeStonePacket._

  var pType: HomeStonePacketType = _
  var secondsRemaining: Int = -1
  var openGUI: Boolean = false

  override def write(out: MessageOut[_]) {
    out.writeByte(pType.id.toByte)
    if (pType == HomeStonePacketType.SYNC) {
      out.writeInt(secondsRemaining)
      out.writeBoolean(openGUI)
    }
  }

  override def read(in: MessageIn[_]) {
    val typeNum = in.readByte()
    if (typeNum < 0 || typeNum >= HomeStonePacketType.values.size) throw new InvalidFormatException
    pType = HomeStonePacketType(typeNum)
    if (pType == HomeStonePacketType.SYNC) {
      secondsRemaining = in.readInt()
      openGUI = in.readBoolean()
    }
  }

  override def executeServer(player: EntityPlayerMP) {
    super.executeServer(player)
    if (pType == HomeStonePacketType.SYNC) {
      JaffasFood.proxy.handleSyncPacket(player, secondsRemaining, openGUI)
    } else throw new UnsupportedOperationException
  }

  override def executeClient(player: EntityPlayerSP) {
    super.executeClient(player)
    if (pType == HomeStonePacketType.PORT) {
      val p = player.asInstanceOf[EntityPlayerMP]

      if (CoolDownRegistry.isCoolDownActive(p.getPersistentID, SPAWN_STONE)) {
        Log.printWarning("Possible hacker&|cheater (uses stone on CD): " + PlayerHelper.formatPlayerID(p))
        return
      }

      val stone = getSpawnStone(p)
      if (stone != null) stone.doTeleportAndSetCD(p, stone)
    } else {
      throw new UnsupportedOperationException
    }
  }
}

object HomeStonePacket {
  def getSpawnStone(player: EntityPlayer): ItemSpawnStone = {
    val stack = player.getCurrentEquippedItem
    if (stack == null) {
      Log.printWarning("Possible hacker&|cheater (uses stone without stone): " + PlayerHelper.formatPlayerID(player))
      return null
    }
    val itemInHand = stack.getItem
    if (!itemInHand.isInstanceOf[ItemSpawnStone]) {
      Log.printWarning("Possible hacker&|cheater (uses stone without stone [di]): " + PlayerHelper.formatPlayerID(player))
      return null
    }
    itemInHand.asInstanceOf[ItemSpawnStone]
  }

  def sendPortPacket() {
    val packet = new HomeStonePacket
    packet.pType = HomeStonePacketType.PORT
    packet.sendToServer()
  }

  def sendSyncPacket(player: EntityPlayer, openGUI: Boolean) {
    val packet = new HomeStonePacket
    packet.pType = HomeStonePacketType.SYNC
    packet.secondsRemaining = CoolDownRegistry.getRemainingCoolDownInSeconds(player.getPersistentID, SPAWN_STONE)
    packet.openGUI = openGUI
    packet.sendToClient(player)
  }
}
