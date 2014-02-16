/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block

import monnef.core.common.ContainerRegistry
import monnef.jaffas.power.common.{IProcessingRecipeHandler, ProcessingRecipeHandler}
import net.minecraftforge.oredict.OreDictionary
import net.minecraft.item.ItemStack
import monnef.jaffas.food.JaffasFood
import monnef.jaffas.food.item.JaffaItem
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine
import monnef.jaffas.power.api.IKitchenUnitAppliance

@ContainerRegistry.ContainerTag(slotsCount = 3, containerClassName = "monnef.jaffas.power.block.common.ContainerDoubleBasicProcessingMachine", guiClassName = "monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine")
class TileJuiceMaker extends TileEntityBasicProcessingMachine with IKitchenUnitAppliance {
  def getInvName: String = "jaffas.power.grinder"
}

object TileJuiceMaker {

  import scala.collection.JavaConverters._
  import JaffasFood.getItem

  val bottleItem = getItem(JaffaItem.juiceBottle)
  val bottleItemStack = new ItemStack(bottleItem)
  final val defaultDuration = 150

  private val recipes: ProcessingRecipeHandler = new ProcessingRecipeHandler

  // don't know why is this needed, scala vs. java interoperability problem?
  def addJuiceRecipe(input: ItemStack, output: ItemStack) { addJuiceRecipe(input, output, defaultDuration) }

  def addJuiceRecipe(input: ItemStack, output: ItemStack, duration: Int = defaultDuration) {
    recipes.addRecipe(Array[ItemStack](input, bottleItemStack.copy()), Array[ItemStack](output), duration)
  }

  def addJuiceRecipe(input: JaffaItem, count: Int, output: JaffaItem) {
    addJuiceRecipe(new ItemStack(getItem(input), count), new ItemStack(getItem(output)), 150)
  }

  def addJuiceOreDictRecipe(oreDictId: String, output: ItemStack, duration: Int = defaultDuration) {
    for (stack <- OreDictionary.getOres(oreDictId).asScala) addJuiceRecipe(stack, output.copy, duration)
  }

  def getRecipeHandler: IProcessingRecipeHandler = recipes
}