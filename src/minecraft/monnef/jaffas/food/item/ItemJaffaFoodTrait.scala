/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item

import net.minecraft.item.{EnumAction, ItemStack}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import monnef.core.utils.PlayerHelper
import net.minecraft.potion.PotionEffect
import monnef.core.item.ItemMonnefCore
import monnef.jaffas.food.item.common.IItemFood
import java.util.Random

trait ItemJaffaFoodTrait[Self <: ItemMonnefCore] extends IItemFood {
  this: ItemMonnefCore with ItemJaffaFoodTrait[Self] =>

  import ItemJaffaFoodTrait._

  type RETURN = Self with ItemJaffaFoodTrait[Self]

  def ret = this.asInstanceOf[RETURN]

  private var returnItem: ItemStack = null
  private var isDrink: Boolean = _
  private var healAmount: Int = _
  private var saturation: Float = _
  private var returnChance: Float = _
  private var itemUseDuration: Int = _
  private var alwaysEdible: Boolean = _
  private var potionId: Int = _
  private var potionDuration: Int = _
  private var potionAmplifier: Int = _
  private var potionEffectProbability: Float = _

  initialize()

  private def initialize(): Unit = {
    setMaxStackSize(64)
    this.itemUseDuration = 32
    this.setSecondCreativeTab(CreativeTabs.tabFood)
  }

  def asItem: ItemJaffaFood = this.asInstanceOf[ItemJaffaFood]

  def onFoodEaten(stack: ItemStack, world: World, player: EntityPlayer): Unit = {
    if (this.returnItem != null && this.returnChance > rand.nextFloat()) {
      PlayerHelper.giveItemToPlayer(player, returnItem.copy())
    }
    if (!world.isRemote && this.potionId > 0 && world.rand.nextFloat() < this.potionEffectProbability) {
      player.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier))
    }
  }

  def setReturnItem(returnItem: ItemStack): RETURN = {
    setReturnItem(returnItem, 1f)
    ret
  }

  def setReturnItem(returnItem: ItemStack, chance: Float): RETURN = {
    this.returnItem = returnItem
    this.returnChance = chance
    ret
  }

  override def getItemUseAction(par1ItemStack: ItemStack): EnumAction = if (this.isDrink) EnumAction.drink else EnumAction.eat

  def setIsDrink(): RETURN = {
    this.isDrink = true
    ret
  }

  override def Setup(healAmount: Int, saturation: Float): ItemMonnefCore = {
    this.healAmount = healAmount
    this.saturation = saturation
    this
  }

  override def getHealAmount(): Int = this.healAmount

  override def getSaturationModifier(): Float = this.saturation

  override def onEaten(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    stack.stackSize = stack.stackSize - 1
    player.getFoodStats.addStats(this.getHealAmount(), this.getSaturationModifier())
    world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F)
    this.onFoodEaten(stack, world, player)
    stack
  }

  override def getMaxItemUseDuration(par1ItemStack: ItemStack): Int = this.itemUseDuration

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if (player.canEat(this.alwaysEdible)) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack))
    }
    stack
  }

  def setPotionEffect(potionId: Int, duration: Int, amplifier: Int, probability: Float): RETURN = {
    this.potionId = potionId
    this.potionDuration = duration
    this.potionAmplifier = amplifier
    this.potionEffectProbability = probability
    ret
  }

  def setAlwaysEdible(): RETURN = {
    this.alwaysEdible = true
    ret
  }
}

object ItemJaffaFoodTrait {
  val rand = new Random
}