package monnef.jaffas.food.item

import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import monnef.core.utils.scalagameutils._
import net.minecraftforge.fluids.{FluidContainerRegistry, IFluidBlock, FluidStack}
import net.minecraft.util.MovingObjectPosition
import monnef.core.utils.{BlockHelper, IntegerCoordinates}
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.init.Blocks
import java.util
import net.minecraft.creativetab.CreativeTabs
import monnef.jaffas.food.common.ContentHolder

class ItemSpongeCloth extends ItemJaffaBase {

  import ItemSpongeCloth._

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    //super.onItemRightClick(stack, world, player)
    val isEmptyFlag = isEmpty(stack)
    val obj = getMovingObjectPositionFromPlayer(world, player, isEmptyFlag)
    if (obj == null) {
      stack
    } else {
      if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
        val hitPos = new IntegerCoordinates(world, obj)
        val wantedBlockPos = hitPos.shiftInDirectionBy(ForgeDirection.getOrientation(obj.sideHit), 1)
        val (x, y, z) = (wantedBlockPos.getX, wantedBlockPos.getY, wantedBlockPos.getZ)
        wantedBlockPos.getBlock match {
          case fluidBlock: IFluidBlock =>
            val fluidStack = fluidBlock.drain(world, x, y, z, false)
            val currentFluidStack = getFluid(stack)
            val currentFluidAmount = currentFluidStack.map(_.amount).getOrElse(0)
            val newAmount = fluidStack.amount + currentFluidAmount
            val newFluidCompatible = currentFluidStack.isEmpty || currentFluidStack.get.containsFluid(fluidStack)
            if (newFluidCompatible && newAmount <= FluidContainerRegistry.BUCKET_VOLUME) {
              var drainedFluidStack = fluidBlock.drain(world, x, y, z, true)
              if (drainedFluidStack.amount == 0) drainedFluidStack = fluidStack // fixing Forge bug
              drainedFluidStack.amount += currentFluidAmount
              setFluid(stack, drainedFluidStack)
              stack
            } else stack

          case _ => stack
        }
      } else stack
    }
  }

  def isEmpty(stack: ItemStack): Boolean = {
    stack.initTag()
    getFluid(stack).isEmpty
  }

  def getFluid(stack: ItemStack): Option[FluidStack] = {
    stack.initTag()
    if (stack.getTagCompound.hasKey(FLUID_KEY)) {
      val fluidTag = stack.getTagCompound.getCompoundTag(FLUID_KEY)
      val fluid = FluidStack.loadFluidStackFromNBT(fluidTag)
      if (fluid == null) None
      else Some(fluid)
    } else None
  }

  def setFluid(stack: ItemStack, fluid: FluidStack) {
    stack.initTag()
    val tag = stack.getTagCompound
    tag.setTag(FLUID_KEY, fluid.writeToNBT(tag.getCompoundTag(FLUID_KEY)))
  }

  override def addInformationCustom(stack: ItemStack, player: EntityPlayer, result: util.List[String], par4: Boolean) {
    super.addInformationCustom(stack, player, result, par4)
    if (isEmpty(stack)) {
      result.add("Empty")
    } else {
      val fl = getFluid(stack).get
      result.add(fl.getFluid.getLocalizedName)
      result.add(fl.amount + "mB")
    }
  }

  override def getSubItems(item: Item, tab: CreativeTabs, result: util.List[_]) {
    val r = result.asInstanceOf[util.List[ItemStack]]

    val empty = new ItemStack(this)
    r.add(empty)

    val wol = new ItemStack(this)
    if (ContentHolder.waterOfLife == null) throw new RuntimeException("Water of Life fluid not yet created.")
    setFluid(wol, new FluidStack(ContentHolder.waterOfLife, FluidContainerRegistry.BUCKET_VOLUME))
    r.add(wol)
  }
}

object ItemSpongeCloth {
  final val FLUID_KEY = "fluid"
}