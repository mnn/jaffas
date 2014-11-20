package monnef.jaffas.food.common

import net.minecraftforge.event.entity.player.PlayerEvent
import monnef.jaffas.food.block.BlockSpecialWeb
import monnef.core.utils.ItemHelper
import cpw.mods.fml.common.eventhandler.SubscribeEvent

class CobWebHarvestingHandler {
  @SubscribeEvent
  def onBlockBreakingStart(event: PlayerEvent.BreakSpeed) {
    if (event.block.isInstanceOf[BlockSpecialWeb] &&
      event.entityPlayer != null &&
      ItemHelper.isSword(event.entityPlayer.getCurrentEquippedItem.getItem)) {
      event.newSpeed = 15
    }
  }
}
