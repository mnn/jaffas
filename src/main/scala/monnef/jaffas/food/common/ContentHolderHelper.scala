package monnef.jaffas.food.common

import monnef.jaffas.food.block.BlockJaffaFiniteFluid
import monnef.core.fluid.FakeFluidRegistry
import net.minecraftforge.fluids.FluidRegistry
import net.minecraft.init.Blocks
import monnef.jaffas.food.block.BlockJaffaFiniteFluid.{OrangeBubble, WhiteBubble, GreenBubble}

object ContentHolderHelper {
  def createFluids() {
    import ContentHolder._

    corrosiveGoo = JaffaFluid.createAndRegister("corrosiveGoo")
    corrosiveGoo.setViscosity(7000)
    blockCorrosiveGoo = BlockJaffaFiniteFluid.createAndRegister(corrosiveGoo, 299)
    blockCorrosiveGoo.setIsPoisonous().setBubbles(GreenBubble).setDestroysBlocks()

    miningGoo = JaffaFluid.createAndRegister("miningGoo")
    miningGoo.setViscosity(2000)
    blockMiningGoo = BlockJaffaFiniteFluid.createAndRegister(miningGoo, 301)
    blockMiningGoo.setIsPoisonous().setHasMiningBonus().setBubbles(GreenBubble).setDestroysBlocks().setDropsDestroyedBlocks()

    unstableGoo = JaffaFluid.createAndRegister("unstableGoo")
    unstableGoo.setLuminosity(7).setViscosity(3000)
    blockUnstableGoo = BlockJaffaFiniteFluid.createAndRegister(unstableGoo, 303)
    blockUnstableGoo.setIsPoisonous().setDoesBurn().setBubbles(OrangeBubble).setDestroysBlocks()

    waterOfLife = JaffaFluid.createAndRegister("waterOfLife")
    waterOfLife.setLuminosity(1)
    blockWaterOfLife = BlockJaffaFiniteFluid.createAndRegister(waterOfLife, 297)
    blockWaterOfLife.setDoesHeal().setBubbles(WhiteBubble)

    FakeFluidRegistry.register(FluidRegistry.WATER, Blocks.water, Blocks.flowing_water)
    FakeFluidRegistry.register(FluidRegistry.LAVA, Blocks.lava, Blocks.flowing_lava)
  }
}
