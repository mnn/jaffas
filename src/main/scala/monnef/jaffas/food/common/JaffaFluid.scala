package monnef.jaffas.food.common

import net.minecraftforge.fluids.{FluidRegistry, Fluid}

class JaffaFluid private(_name: String) extends Fluid(_name) {

}

object JaffaFluid {
  def createAndRegister(name: String): JaffaFluid = {
    val f = new JaffaFluid("jaffas." + name)
    FluidRegistry.registerFluid(f)
    f
  }
}
