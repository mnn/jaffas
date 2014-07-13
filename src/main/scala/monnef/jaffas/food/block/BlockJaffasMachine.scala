/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block

import monnef.core.block.BlockMachine
import net.minecraft.block.material.Material

abstract class BlockJaffasMachine(_tex: Int, _mat: Material, _customRenderer: Boolean, _useOwnRenderingId: Boolean) extends BlockMachine(_tex, _mat, _customRenderer, _useOwnRenderingId) with BlockJaffasLike {
}
