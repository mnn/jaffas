/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.material.Material
import net.minecraft.world.IBlockAccess
import monnef.jaffas.trees.common.IconDescriptorTrees

class BlockLeavesBaseJaffas(_id: Int, _texture: Int, _material: Material, var graphicsLevel: Boolean) extends BlockJaffas(_id, _texture, _material) with IconDescriptorTrees {

  override def isOpaqueCube: Boolean = false

  @SideOnly(Side.CLIENT) override def shouldSideBeRendered(par1IBlockAccess: IBlockAccess, par2: Int, par3: Int, par4: Int, par5: Int): Boolean = {
    val i1: Int = par1IBlockAccess.getBlockId(par2, par3, par4)
    if (!this.graphicsLevel && i1 == this.blockID) false
    else super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5)
  }
}