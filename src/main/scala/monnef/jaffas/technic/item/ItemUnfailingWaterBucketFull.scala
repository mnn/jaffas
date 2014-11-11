package monnef.jaffas.technic.item

import monnef.jaffas.technic.JaffasTechnic
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MovingObjectPosition
import monnef.core.utils.IntegerCoordinates
import net.minecraftforge.common.util.ForgeDirection
import monnef.core.fluid.FluidPlacer
import net.minecraftforge.fluids.FluidRegistry
import net.minecraft.init.Blocks

class ItemUnfailingWaterBucketFull(_tex: Int) extends ItemTechnic(_tex) {
  setMaxStackSize(1)
  setContainerItem(JaffasTechnic.unfailingWaterBucketEmpty)

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    val obj = getMovingObjectPositionFromPlayer(world, player, false)
    if (obj != null) {
      if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
        val hitPos = new IntegerCoordinates(world, obj)
        val oneOutPos = hitPos.shiftInDirectionBy(ForgeDirection.getOrientation(obj.sideHit), 1)
        if (oneOutPos.getBlock == Blocks.flowing_water || oneOutPos.getBlock == Blocks.water) {
          oneOutPos.setBlock(Blocks.air)
        }
        if (FluidPlacer.tryPlace(FluidRegistry.getFluidStack(FluidRegistry.WATER.getName, 1000), oneOutPos)) new ItemStack(JaffasTechnic.unfailingWaterBucketEmpty)
        else stack
      } else stack
    } else stack
  }
}
