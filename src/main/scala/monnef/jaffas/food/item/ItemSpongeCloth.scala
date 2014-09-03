package monnef.jaffas.food.item

import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import monnef.core.utils.scalagameutils._
import net.minecraftforge.fluids._
import net.minecraft.util.{IIcon, MovingObjectPosition}
import monnef.core.utils.{LanguageHelper, IntegerCoordinates}
import net.minecraftforge.common.util.ForgeDirection
import java.util
import net.minecraft.creativetab.CreativeTabs
import monnef.jaffas.food.common.ContentHolder
import monnef.core.api.IIntegerCoordinates
import scala.Some
import monnef.core.fluid.{FluidPlacer, FakeFluidRegistry}

class ItemSpongeCloth extends ItemJaffaBase {

  import ItemSpongeCloth._

  maxStackSize = 1
  setIconsCount(2)

  def suckLiquid(stack: ItemStack, pos: IIntegerCoordinates): Option[FluidStack] = {
    val (x, y, z, world) = (pos.getX, pos.getY, pos.getZ, pos.getWorld)
    val block = FakeFluidRegistry.wrapOrPass(pos.getBlock)
    block match {
      case fluidBlock: IFluidBlock =>
        val fluidStack = fluidBlock.drain(world, x, y, z, false)
        fluidStack match {
          case null => None
          case _ =>
            val currentFluidStack = getFluidStack(stack)
            val currentFluidAmount = currentFluidStack.map(_.amount).getOrElse(0)
            val newAmount = fluidStack.amount + currentFluidAmount
            val newFluidCompatible = currentFluidStack.isEmpty || currentFluidStack.get.containsFluid(fluidStack)
            if (newFluidCompatible && newAmount <= FluidContainerRegistry.BUCKET_VOLUME) {
              var drainedFluidStack = fluidBlock.drain(world, x, y, z, true)
              if (drainedFluidStack.amount == 0) drainedFluidStack = fluidStack // fixing Forge bug
              drainedFluidStack.amount += currentFluidAmount
              Some(drainedFluidStack)
            } else None
        }
      case _ => None
    }
  }

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    //super.onItemRightClick(stack, world, player)
    val isEmptyFlag = isEmpty(stack)
    val obj = getMovingObjectPositionFromPlayer(world, player, isEmptyFlag)
    if (obj == null) {
      stack
    } else {
      if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
        def updateStack(newFluid: FluidStack): Boolean = { setFluidStack(stack, newFluid); true }

        val hitPos = new IntegerCoordinates(world, obj)
        val oneOutPos = hitPos.shiftInDirectionBy(ForgeDirection.getOrientation(obj.sideHit), 1)

        val sucked = suckLiquid(stack, hitPos) match {
          case Some(fluid) => updateStack(fluid)
          case None => suckLiquid(stack, oneOutPos) match {
            case Some(fluid) => updateStack(fluid)
            case None => false
          }
        }
        if (!sucked && !isEmptyFlag) {
          if (tryPlaceLiquid(stack, oneOutPos)) setEmpty(stack)
        }
      }
      stack
    }
  }

  def tryPlaceLiquid(stack: ItemStack, pos: IIntegerCoordinates): Boolean = FluidPlacer.tryPlace(getFluidStack(stack).get, pos)

  def setEmpty(stack: ItemStack) {
    stack.initTag()
    stack.getTagCompound.removeTag(FLUID_KEY)
  }

  def isEmpty(stack: ItemStack): Boolean = {
    stack.initTag()
    getFluidStack(stack).isEmpty
  }

  def getFluidStack(stack: ItemStack): Option[FluidStack] = {
    stack.initTag()
    if (stack.getTagCompound.hasKey(FLUID_KEY)) {
      val fluidTag = stack.getTagCompound.getCompoundTag(FLUID_KEY)
      val fluid = FluidStack.loadFluidStackFromNBT(fluidTag)
      if (fluid == null) None
      else Some(fluid)
    } else None
  }

  def setFluidStack(stack: ItemStack, fluid: FluidStack) {
    stack.initTag()
    val tag = stack.getTagCompound
    tag.setTag(FLUID_KEY, fluid.writeToNBT(tag.getCompoundTag(FLUID_KEY)))
  }

  override def addInformationCustom(stack: ItemStack, player: EntityPlayer, result: util.List[String], par4: Boolean) {
    super.addInformationCustom(stack, player, result, par4)
    getFluidStack(stack) match {
      case Some(fluid) =>
        // result.add(fluid.getFluid.getLocalizedName)
        result.add(fluid.amount + "mB")
      case None => result.add("Empty")
    }
  }

  override def getItemStackDisplayName(stack: ItemStack): String = {
    val title = super.getItemStackDisplayName(stack)
    getFluidStack(stack) match {
      case Some(fluid) => LanguageHelper.toLocalFormatted("jaffas.spongeCloth.wet", title, fluid.getFluid.getLocalizedName)
      case None => LanguageHelper.toLocalFormatted("jaffas.spongeCloth.dry", title)
    }
  }

  override def getSubItems(item: Item, tab: CreativeTabs, result: util.List[_]) {
    def createStack(fluid: Fluid): ItemStack = {
      val stack = new ItemStack(this)
      if (fluid == null) throw new RuntimeException("Fluid not yet created.")
      setFluidStack(stack, new FluidStack(ContentHolder.waterOfLife, FluidContainerRegistry.BUCKET_VOLUME))
      stack
    }

    val r = result.asInstanceOf[util.List[ItemStack]]
    r.add(new ItemStack(this))
    r.add(createStack(ContentHolder.waterOfLife))
    r.add(createStack(ContentHolder.corrosiveGoo))
    r.add(createStack(ContentHolder.miningGoo))
    r.add(createStack(ContentHolder.unstableGoo))
  }

  override def getIconIndex(stack: ItemStack): IIcon = icons(if (isEmpty(stack)) 0 else 1)
}

object ItemSpongeCloth {
  final val FLUID_KEY = "fluid"
}
