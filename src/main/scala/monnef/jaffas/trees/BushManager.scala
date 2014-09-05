package monnef.jaffas.trees

import monnef.jaffas.trees.common.{EatableType, DropType, BushInfo}
import net.minecraft.item.Item
import monnef.jaffas.trees.common.EatableType._
import monnef.jaffas.trees.common.DropType._
import monnef.jaffas.trees.BushType._
import java.util

object BushManager {
  var bushesList: util.EnumMap[BushType, BushInfo] = new util.EnumMap[BushType, BushInfo](classOf[BushType])

  private def addBushInfo(`type`: BushType, name: String, seedsTexture: Int, plantTexture: Int, fruitTexture: Int, product: Item, lastPhase: Int, renderer: Int, eatable: EatableType, drop: DropType) {
    val info: BushInfo = new BushInfo
    info.name = name
    info.seedsTexture = seedsTexture
    info.plantTexture = plantTexture
    info.fruitTexture = fruitTexture
    info.product = product
    info.lastPhase = lastPhase
    info.renderer = renderer
    info.`type` = `type`
    info.eatable = eatable
    info.drop = drop
    bushesList.put(`type`, info)
  }

  def populateBushInfo() {
    val iconIndex = Stream.from(0).map(190 + _ * 5).toIterator
    addBushInfo(Coffee, "coffee", 34, iconIndex.next(), 128, null, 4, 1, NotEatable, DropsFromGrass)
    addBushInfo(Strawberry, "strawberry", 34, iconIndex.next(), 129, null, 4, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Onion, "onion", 34, iconIndex.next(), 130, null, 4, 1, NotEatable, DropsFromGrass)
    addBushInfo(Paprika, "paprika", 34, iconIndex.next(), 131, null, 4, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Raspberry, "raspberry", 34, iconIndex.next(), 132, null, 4, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Tomato, "tomato", 34, iconIndex.next(), 133, null, 4, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Mustard, "mustard", 34, iconIndex.next(), 134, null, 4, 1, NotEatable, DropsFromGrass)
    addBushInfo(Peanuts, "peanuts", 34, iconIndex.next(), 135, null, 4, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Pea, "pea", 34, iconIndex.next(), 136, null, 4, 1, EatableNormal, DropsFromGrass)
    addBushInfo(Bean, "bean", 34, iconIndex.next(), 137, null, 4, 1, NotEatable, DropsFromGrass)
  }
}
