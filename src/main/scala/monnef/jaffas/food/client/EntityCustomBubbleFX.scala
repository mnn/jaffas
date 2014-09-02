package monnef.jaffas.food.client

import net.minecraft.client.particle.EntitySplashFX
import net.minecraft.world.World
import monnef.core.utils.RandomHelper._

class EntityCustomBubbleFX(world: World, x: Double, y: Double, z: Double, motX: Double, motY: Double, motZ: Double) extends EntitySplashFX(world, x, y, z, motX, motY, motZ) {
  setParticleTextureIndex(32)
  particleGravity = 0
  particleMaxAge = generateRandomFromBaseAndSpread(100, 30)
  setSize(0.01F, 0.01F)
  motionY = motY

  def configureColor(red: Float, green: Float, blue: Float, alpha: Float): EntityCustomBubbleFX = {
    particleRed = red
    particleGreen = green
    particleBlue = blue
    particleAlpha = alpha
    this
  }

}

object EntityCustomBubbleFX {
  final val GOO_ALPHA = .5f
  final val WATER_ALPHA = .1f

  def createOnBlock(world: World, x: Int, y: Int, z: Int): EntityCustomBubbleFX = {
    new EntityCustomBubbleFX(world, x + rand.nextFloat(), y + 1, z + rand.nextFloat(),
      generateRandomFromSymmetricInterval(.03f),
      .03 + generateRandomFromSymmetricInterval(.01f),
      generateRandomFromSymmetricInterval(.03f))
  }

  def createWhiteOnSurface(world: World, x: Int, y: Int, z: Int): EntityCustomBubbleFX = {
    createOnBlock(world, x, y, z).configureColor(1, 1, 1, WATER_ALPHA)
  }

  def createGreenOnSurface(world: World, x: Int, y: Int, z: Int): EntityCustomBubbleFX = {
    createOnBlock(world, x, y, z).configureColor(.2f, 1, .2f, GOO_ALPHA)
  }

  def createOrangeOnSurface(world: World, x: Int, y: Int, z: Int): EntityCustomBubbleFX = {
    createOnBlock(world, x, y, z).configureColor(1f, .5f, .05f, GOO_ALPHA)
  }
}
