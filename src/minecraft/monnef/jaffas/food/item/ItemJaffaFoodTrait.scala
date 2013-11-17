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
import monnef.core.item.ItemMonnefCore
import monnef.jaffas.food.item.common.IItemFood
import java.util.Random
import net.minecraft.potion.PotionEffect

trait ItemJaffaFoodTrait[Self <: ItemMonnefCore] extends IItemFood {
  this: ItemMonnefCore with ItemJaffaFoodTrait[Self] =>

  import ItemJaffaFoodTrait._

  type RETURN = Self with ItemJaffaFoodTrait[Self]

  def ret = this.asInstanceOf[RETURN]

  private var returnItem: ItemStack = null
  private var isDrink: Boolean = false
  private var healAmount: Int = 0
  private var saturation: Float = 0
  private var returnChance: Float = 0
  private var itemUseDuration: Int = BASE_USE_DURATION
  private var alwaysEdible: Boolean = false

  case class FoodPotionEffect(potionId: Int, potionDuration: Int, potionAmplifier: Int, potionEffectProbability: Float)

  private var potionEffects = List.empty[FoodPotionEffect]

  initialize()

  private def initialize(): Unit = {
    setMaxStackSize(64)
    this.setSecondCreativeTab(CreativeTabs.tabFood)
  }

  def asItem: Self = this.asInstanceOf[Self]

  def addPotionEffect(id: Int, duration: Int, amplifier: Int, probability: Float): RETURN = {
    potionEffects ::= FoodPotionEffect(id, duration, amplifier, probability)
    ret
  }

  def onFoodEaten(stack: ItemStack, world: World, player: EntityPlayer): Unit = {
    if (this.returnItem != null && this.returnChance > rand.nextFloat()) {
      PlayerHelper.giveItemToPlayer(player, returnItem.copy())
    }
    if (!world.isRemote) {
      for {
        e <- potionEffects
        if e.potionId > 0 && world.rand.nextFloat() < e.potionEffectProbability
      } player.addPotionEffect(new PotionEffect(e.potionId, e.potionDuration * 20, e.potionAmplifier))
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
      player.setItemInUse(stack, asItem.getMaxItemUseDuration(stack))
    }
    stack
  }

  def setAlwaysEdible(): RETURN = {
    this.alwaysEdible = true
    ret
  }

  def setItemUseDuration(rel: Float): RETURN = {
    this.itemUseDuration = (BASE_USE_DURATION * rel).round
    ret
  }
}

object ItemJaffaFoodTrait {
  val BASE_USE_DURATION = 32
  val rand = new Random
}