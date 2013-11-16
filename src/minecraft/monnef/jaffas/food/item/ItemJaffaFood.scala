package monnef.jaffas.food.item

import monnef.core.utils.PlayerHelper
import monnef.jaffas.food.item.common.IItemFood
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World


class ItemJaffaFood(__id: Int) extends ItemJaffaBase(__id) with IItemFood {
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
    maxStackSize = 64
    this.itemUseDuration = 32
    this.setSecondCreativeTab(CreativeTabs.tabFood)
  }

  def onFoodEaten(stack: ItemStack, world: World, player: EntityPlayer): Unit = {
    if (this.returnItem != null && this.returnChance > Item.itemRand.nextFloat()) {
      PlayerHelper.giveItemToPlayer(player, returnItem.copy())
    }
    if (!world.isRemote && this.potionId > 0 && world.rand.nextFloat() < this.potionEffectProbability) {
      player.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier))
    }
  }

  def setReturnItem(returnItem: ItemStack): ItemJaffaFood = {
    setReturnItem(returnItem, 1f)
    this
  }

  def setReturnItem(returnItem: ItemStack, chance: Float): ItemJaffaFood = {
    this.returnItem = returnItem
    this.returnChance = chance
    this
  }

  override def getItemUseAction(par1ItemStack: ItemStack): EnumAction = if (this.isDrink) EnumAction.drink else EnumAction.eat

  def setIsDrink(): ItemJaffaFood = {
    this.isDrink = true
    this
  }

  override def Setup(healAmount: Int, saturation: Float): Item = {
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

  def setPotionEffect(potionId: Int, duration: Int, amplifier: Int, probability: Float): ItemJaffaFood = {
    this.potionId = potionId
    this.potionDuration = duration
    this.potionAmplifier = amplifier
    this.potionEffectProbability = probability
    this
  }

  def setAlwaysEdible(): ItemJaffaFood = {
    this.alwaysEdible = true
    this
  }
}
