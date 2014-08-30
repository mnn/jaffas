package monnef.jaffas.food.common

import net.minecraftforge.fluids._
import monnef.core.utils.LanguageHelper
import net.minecraft.block.Block

class JaffaFluid protected(_name: String) extends Fluid(_name) {
  private[this] lazy val unlocalizedNameMatchingBlock = "tile." + getUnlocalizedName + ".name"

  override def getLocalizedName: String = {
    if (unlocalizedNameMatchingBlock == null) "" else LanguageHelper.toLocal(unlocalizedNameMatchingBlock)
  }
}

object JaffaFluid {
  def createAndRegister(name: String): JaffaFluid = {
    val f = new JaffaFluid("jaffas." + name)
    register(f)
    f
  }

  def register(jf: JaffaFluid) {
    FluidRegistry.registerFluid(jf)
  }
}

class FakeJaffaFluid(_name: String, block: Block) extends JaffaFluid(_name) {
  setBlock(block)
}
