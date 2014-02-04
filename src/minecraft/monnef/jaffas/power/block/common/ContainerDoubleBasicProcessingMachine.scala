/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common

import monnef.core.block.{TileMachineWithInventory, ContainerMachine}
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.{IInventory, Slot}
import monnef.core.client.SlotOutput

class ContainerDoubleBasicProcessingMachine(_invPlayer: InventoryPlayer, _te: TileMachineWithInventory) extends ContainerMachine(_invPlayer, _te) {
  final val SLOT_INPUT_EDIBLE = 0
  final val SLOT_INPUT_BOTTLE = 1
  final val SLOT_OUTPUT = 2

  override def constructSlots(inv: IInventory) {
    addSlotToContainer(new Slot(inv, SLOT_INPUT_EDIBLE, 42, 35))
    addSlotToContainer(new Slot(inv, SLOT_INPUT_BOTTLE, 42, 55))
    addSlotToContainer(new SlotOutput(inv, SLOT_OUTPUT, 111, 35))
  }

  def getOutputSlot(outputNumber: Int): Slot = getSlot(getStartIndexOfOutput + outputNumber)

  def getInputSlot(number: Int): Slot = getSlot(number)
}
