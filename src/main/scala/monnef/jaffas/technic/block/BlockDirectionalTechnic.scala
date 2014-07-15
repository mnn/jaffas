package monnef.jaffas.technic.block

import monnef.jaffas.food.block.BlockJDirectional
import monnef.jaffas.technic.JaffasTechnic
import net.minecraft.block.material.Material
import monnef.jaffas.technic.common.IconDescriptorTechnic

class BlockDirectionalTechnic(_textureStart: Int, _texturesCountPerSet: Int, _material: Material, _type: BlockJDirectional.TextureMappingType, _textureSetsCount: Int) extends
BlockJDirectional(_textureStart, _texturesCountPerSet, _material, _type, _textureSetsCount) with IconDescriptorTechnic {
  setCreativeTab(JaffasTechnic.instance.creativeTab)

  def this(id: Int, textureStart: Int, texturesCountPerSet: Int, material: Material, `type`: BlockJDirectional.TextureMappingType) {
    this(id, textureStart, texturesCountPerSet, material, `type`, 1)
  }
}