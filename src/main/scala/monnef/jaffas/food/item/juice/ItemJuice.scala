/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item.juice

import monnef.jaffas.food.item.common.ItemJaffaMultiBase
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import java.util
import monnef.core.MonnefCorePlugin
import net.minecraft.util.{IIcon, MathHelper}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import monnef.jaffas.food.item.ItemJaffaFoodTrait
import monnef.jaffas.food.item.JaffaItem._
import monnef.jaffas.food.JaffasFood.getItem
import monnef.core.utils.StringsHelper

class ItemJuice extends ItemJaffaMultiBase with JuiceLike with ItemJaffaFoodTrait[ItemJuice] {
  // TODO: proper names
  val getSubNames: Array[String] = Juices.juiceTitles.map(StringsHelper.generateNameFromTitle).toArray

  val getSubTitles: Array[String] = Juices.juiceTitles.toArray

  this.setupFoodValues(12, 1f).setIsDrink().setReturnItem(new ItemStack(getItem(juiceBottle))).setItemUseDuration(1.5f)

  override def getSubItemsCount: Int = Juices.juiceCount

  override def addInformationCustom(stack: ItemStack, player: EntityPlayer, result: util.List[String], par4: Boolean) {
    super.addInformationCustom(stack, player, result, par4)
    if (MonnefCorePlugin.debugEnv) {
      val l = result.asInstanceOf[util.List[String]]
      l.add("* NEW *")
    }
  }

  def getMultiItemIconOffset(dmg: Int): Int = Juices.juiceOffsets(dmg)
}

trait JuiceLikeBase {
  def initMulti()

  def getIconFromDamage(damage: Int): IIcon
}

trait JuiceLike extends JuiceLikeBase {
  this: ItemJaffaMultiBase =>

  abstract override def initMulti() {
    super.initMulti()
    setIconsCount(Juices.juiceMaxOffset + 1)
  }

  def getMultiItemIconOffset(dmg: Int): Int

  @SideOnly(Side.CLIENT)
  abstract override def getIconFromDamage(damage: Int): IIcon = {
    val dmg = MathHelper.clamp_int(damage, 0, Juices.juiceCount)
    val iconNum = MathHelper.clamp_int(getMultiItemIconOffset(dmg), 0, getSubItemsCount)
    getCustomIcon(iconNum)
  }
}

