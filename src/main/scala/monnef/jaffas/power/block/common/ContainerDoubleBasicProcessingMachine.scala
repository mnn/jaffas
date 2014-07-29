/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common

import monnef.core.block.{TileMachineWithInventory, ContainerMachine}
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.{IInventory, Slot}
import monnef.core.client.SlotOutput

class ContainerDoubleBasicProcessingMachine(_invPlayer: InventoryPlayer, _te: TileEntityBasicProcessingMachine) extends ContainerBasicProcessingMachine(_invPlayer, _te) {
  final val SLOT_INPUT_EDIBLE = 0
  final val SLOT_INPUT_BOTTLE = 1
  final val SLOT_OUTPUT = 2

  override def constructSlotsFromInventory(inv: IInventory) {
    addSlotToContainer(new Slot(inv, SLOT_INPUT_EDIBLE, 42, 25))
    addSlotToContainer(new Slot(inv, SLOT_INPUT_BOTTLE, 42, 45))
    addSlotToContainer(new SlotOutput(inv, SLOT_OUTPUT, 111, 35))
  }
}
