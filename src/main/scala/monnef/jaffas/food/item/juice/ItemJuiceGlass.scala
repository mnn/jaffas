/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.juice

import monnef.jaffas.food.item.common.ItemJaffaMultiBase
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import monnef.core.MonnefCorePlugin
import java.util
import monnef.jaffas.food.item.ItemJaffaFoodTrait
import monnef.jaffas.food.item.JaffaItem._
import monnef.jaffas.food.JaffasFood.getItem

class ItemJuiceGlass extends ItemJaffaMultiBase with JuiceLike with ItemJaffaFoodTrait[ItemJuiceGlass] {
  // TODO: proper names
  val getSubNames: Array[String] = Juices.glassTitles.toArray

  val getSubTitles: Array[String] = Juices.glassTitles.toArray

  this.setupFoodValues(5, 0.25f).setIsDrink().setReturnItem(new ItemStack(getItem(glassEmpty)))

  override def getSubItemsCount: Int = Juices.juiceCount

  override def addInformation(stack: ItemStack, player: EntityPlayer, result: util.List[_], par4: Boolean) {
    super.addInformation(stack, player, result, par4)
    if (MonnefCorePlugin.debugEnv) {
      val l = result.asInstanceOf[util.List[String]]
      l.add("* NEW *")
    }
  }

  def getMultiItemIconOffset(dmg: Int): Int = Juices.glassOffsets(dmg)
}
