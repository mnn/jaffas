package monnef.jaffas.trees

import monnef.jaffas.trees.common.{EatableType, DropType, BushInfo}
import net.minecraft.item.Item
import monnef.jaffas.trees.common.EatableType._
import monnef.jaffas.trees.common.DropType._
import monnef.jaffas.trees.BushType._
import java.util

object BushManager {
  var bushesList: util.EnumMap[BushType, BushInfo] = new util.EnumMap[BushType, BushInfo](classOf[BushType])

  private def addBushInfo(`type`: BushType, name: String, seedsTexture: Int, plantTexture: Int, fruitTexture: Int, product: Item, phases: Int, renderer: Int, eatable: EatableType, drop: DropType) {
    val info: BushInfo = new BushInfo
    info.name = name
    info.seedsTexture = seedsTexture
    info.plantTexture = plantTexture
    info.fruitTexture = fruitTexture
    info.product = product
    info.phases = phases
    info.renderer = renderer
    info.`type` = `type`
    info.eatable = eatable
    info.drop = drop
    bushesList.put(`type`, info)
  }

  def populateBushInfo() {
    addBushInfo(Coffee, "coffee", 34, 190, 128, null, 2, 1, NotEatable, DropsFromGrass)
    addBushInfo(Strawberry, "strawberry", 34, 190 + 5 * 1, 129, null, 2, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Onion, "onion", 34, 102, 130, null, 2, 1, NotEatable, DropsFromGrass)
    addBushInfo(Paprika, "paprika", 34, 105, 131, null, 2, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Raspberry, "raspberry", 34, 108, 132, null, 2, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Tomato, "tomato", 34, 111, 133, null, 2, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Mustard, "mustard", 34, 114, 134, null, 2, 1, NotEatable, DropsFromGrass)
    addBushInfo(Peanuts, "peanuts", 34, 117, 135, null, 2, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Pea, "pea", 34, 120, 136, null, 2, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Bean, "bean", 34, 123, 137, null, 2, 1, NotEatable, DropsFromGrass)
  }
}
