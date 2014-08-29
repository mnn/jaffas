package monnef.jaffas.food.common

import net.minecraftforge.fluids.{FluidRegistry, Fluid}
import net.minecraft.util.StatCollector

class JaffaFluid private(_name: String) extends Fluid(_name) {
  private[this] lazy val unlocalizedNameMatchingBlock = "tile." + getUnlocalizedName + ".name"

  override def getLocalizedName: String = {
    if (unlocalizedNameMatchingBlock == null) "" else StatCollector.translateToLocal(unlocalizedNameMatchingBlock)
  }
}

object JaffaFluid {
  def createAndRegister(name: String): JaffaFluid = {
    val f = new JaffaFluid("jaffas." + name)
    FluidRegistry.registerFluid(f)
    f
  }
}
