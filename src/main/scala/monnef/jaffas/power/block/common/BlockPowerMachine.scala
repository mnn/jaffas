/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common

import monnef.jaffas.food.block.BlockJaffasMachine
import net.minecraft.block.material.Material
import monnef.jaffas.power.common.IconDescriptorPower

abstract class BlockPowerMachine(_tex: Int, _mat: Material, _customRenderer: Boolean, _useOwnRenderingId: Boolean) extends BlockJaffasMachine(_tex, _mat, _customRenderer, _useOwnRenderingId) with IconDescriptorPower {
}
