package monnef.jaffas.technic.entity

import java.util

import cpw.mods.fml.relauncher.{Side, SideOnly}
import monnef.core.utils.RandomHelper
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.item.EntityBoat
import net.minecraft.entity.{Entity, EntityLiving, EntityLivingBase}
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.Item
import net.minecraft.util.{AxisAlignedBB, MathHelper}

trait IRelaxedAccess {
  this: Entity =>
  def setRotationPublic(yaw: Float, pitch: Float): Unit
}

trait EntityTurnableBase {
  def onUpdate(): Unit

  protected def entityInit(): Unit
}

/**
 * Very heavily based on EntityBoat.
 * A lot of attention needed!
 */
trait EntityTurnable extends EntityTurnableBase {
  this: EntityLiving with IRelaxedAccess =>

  private var isBoatEmpty: Boolean = false
  private var speedMultiplier: Double = .0
  private var boatPosRotationIncrements: Int = 0
  @SideOnly(Side.CLIENT) private var velocityX: Double = .0
  @SideOnly(Side.CLIENT) private var velocityY: Double = .0
  @SideOnly(Side.CLIENT) private var velocityZ: Double = .0
  private var boatX: Double = .0
  private var boatY: Double = .0
  private var boatZ: Double = .0
  private var boatYaw: Double = .0
  private var boatPitch: Double = .0

  protected abstract override def entityInit() {
    super.entityInit()
    this.getDataWatcher.addObject(17, new Integer(0))
    this.getDataWatcher.addObject(18, new Integer(1))
    this.getDataWatcher.addObject(19, Float.box(0))
  }

  /**
   * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
   * posY, posZ, yaw, pitch
   */
  @SideOnly(Side.CLIENT) override def setPositionAndRotation2(p_70056_1_ : Double, p_70056_3_ : Double, p_70056_5_ : Double, p_70056_7_ : Float, p_70056_8_ : Float, p_70056_9_ : Int) {
    if (this.isBoatEmpty) {
      this.boatPosRotationIncrements = p_70056_9_ + 5
    }
    else {
      val d3: Double = p_70056_1_ - this.posX
      val d4: Double = p_70056_3_ - this.posY
      val d5: Double = p_70056_5_ - this.posZ
      val d6: Double = d3 * d3 + d4 * d4 + d5 * d5
      if (d6 <= 1.0D) {
        return
      }
      this.boatPosRotationIncrements = 3
    }
    this.boatX = p_70056_1_
    this.boatY = p_70056_3_
    this.boatZ = p_70056_5_
    this.boatYaw = p_70056_7_.asInstanceOf[Double]
    this.boatPitch = p_70056_8_.asInstanceOf[Double]
    this.motionX = this.velocityX
    this.motionY = this.velocityY
    this.motionZ = this.velocityZ
  }

  /**
   * Sets the velocity to the args. Args: x, y, z
   */
  @SideOnly(Side.CLIENT) override def setVelocity(p_70016_1_ : Double, p_70016_3_ : Double, p_70016_5_ : Double) {
    this.velocityX = {this.motionX = p_70016_1_; this.motionX}
    this.velocityY = {this.motionY = p_70016_3_; this.motionY}
    this.velocityZ = {this.motionZ = p_70016_5_; this.motionZ}
  }

  /**
   * Called to update the entity's position/logic.
   */
  abstract override def onUpdate() {
    super.onUpdate()
    if (this.getTimeSinceHit > 0) {
      this.setTimeSinceHit(this.getTimeSinceHit - 1)
    }
    if (this.getDamageTaken > 0.0F) {
      this.setDamageTaken(this.getDamageTaken - 1.0F)
    }
    this.prevPosX = this.posX
    this.prevPosY = this.posY
    this.prevPosZ = this.posZ
    val b0: Byte = 5
    var d0: Double = 0.0D;
    {
      var i: Int = 0
      while (i < b0) {
        {
          val d1: Double = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (i + 0).asInstanceOf[Double] / b0.asInstanceOf[Double] - 0.125D
          val d3: Double = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (i + 1).asInstanceOf[Double] / b0.asInstanceOf[Double] - 0.125D
          val axisalignedbb: AxisAlignedBB = AxisAlignedBB.getBoundingBox(this.boundingBox.minX, d1, this.boundingBox.minZ, this.boundingBox.maxX, d3, this.boundingBox.maxZ)
          if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
            d0 += 1.0D / b0.asInstanceOf[Double]
          }
        }
        {i += 1; i}
      }
    }
    val d10: Double = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)
    var d2: Double = .0
    var d4: Double = .0
    var j: Int = 0
    if (d10 > 0.26249999999999996D) {
      d2 = Math.cos(this.rotationYaw.asInstanceOf[Double] * Math.PI / 180.0D)
      d4 = Math.sin(this.rotationYaw.asInstanceOf[Double] * Math.PI / 180.0D);
      {
        j = 0
        while (j.asInstanceOf[Double] < 1.0D + d10 * 60.0D) {
          {
            val d5: Double = (RandomHelper.rand.nextFloat * 2.0F - 1.0F).asInstanceOf[Double]
            val d6: Double = (RandomHelper.rand.nextInt(2) * 2 - 1).asInstanceOf[Double] * 0.7D
            var d8: Double = .0
            var d9: Double = .0
            if (RandomHelper.rand.nextBoolean) {
              d8 = this.posX - d2 * d5 * 0.8D + d4 * d6
              d9 = this.posZ - d4 * d5 * 0.8D - d2 * d6
              this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ)
            }
            else {
              d8 = this.posX + d2 + d4 * d5 * 0.7D
              d9 = this.posZ + d4 - d2 * d5 * 0.7D
              this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ)
            }
          }
          {j += 1; j}
        }
      }
    }
    var d11: Double = .0
    var d12: Double = .0
    if (this.worldObj.isRemote && this.isBoatEmpty) {
      if (this.boatPosRotationIncrements > 0) {
        d2 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements.asInstanceOf[Double]
        d4 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements.asInstanceOf[Double]
        d11 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements.asInstanceOf[Double]
        d12 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw.asInstanceOf[Double])
        this.rotationYaw = (this.rotationYaw.asInstanceOf[Double] + d12 / this.boatPosRotationIncrements.asInstanceOf[Double]).asInstanceOf[Float]
        this.rotationPitch = (this.rotationPitch.asInstanceOf[Double] + (this.boatPitch - this.rotationPitch.asInstanceOf[Double]) / this.boatPosRotationIncrements.asInstanceOf[Double]).asInstanceOf[Float]
        this.boatPosRotationIncrements -= 1
        this.setPosition(d2, d4, d11)
        setRotationPublic(rotationYaw, rotationPitch)
      }
      else {
        d2 = this.posX + this.motionX
        d4 = this.posY + this.motionY
        d11 = this.posZ + this.motionZ
        this.setPosition(d2, d4, d11)
        if (this.onGround) {
          this.motionX *= 0.5D
          this.motionY *= 0.5D
          this.motionZ *= 0.5D
        }
        this.motionX *= 0.9900000095367432D
        this.motionY *= 0.949999988079071D
        this.motionZ *= 0.9900000095367432D
      }
    }
    else {
      if (d0 < 1.0D) {
        d2 = d0 * 2.0D - 1.0D
        this.motionY += 0.03999999910593033D * d2
      }
      else {
        if (this.motionY < 0.0D) {
          this.motionY /= 2.0D
        }
        this.motionY += 0.007000000216066837D
      }
      if (this.riddenByEntity != null && this.riddenByEntity.isInstanceOf[EntityLivingBase]) {
        val entitylivingbase: EntityLivingBase = this.riddenByEntity.asInstanceOf[EntityLivingBase]
        val f: Float = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F
        this.motionX += -Math.sin((f * Math.PI.asInstanceOf[Float] / 180.0F).asInstanceOf[Double]) * this.speedMultiplier * entitylivingbase.moveForward.asInstanceOf[Double] * 0.05000000074505806D
        this.motionZ += Math.cos((f * Math.PI.asInstanceOf[Float] / 180.0F).asInstanceOf[Double]) * this.speedMultiplier * entitylivingbase.moveForward.asInstanceOf[Double] * 0.05000000074505806D
      }
      d2 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)
      if (d2 > 0.35D) {
        d4 = 0.35D / d2
        this.motionX *= d4
        this.motionZ *= d4
        d2 = 0.35D
      }
      if (d2 > d10 && this.speedMultiplier < 0.35D) {
        this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D
        if (this.speedMultiplier > 0.35D) {
          this.speedMultiplier = 0.35D
        }
      }
      else {
        this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D
        if (this.speedMultiplier < 0.07D) {
          this.speedMultiplier = 0.07D
        }
      }
      var l: Int = 0;
      {
        l = 0
        while (l < 4) {
          {
            val i1: Int = MathHelper.floor_double(this.posX + ((l % 2).asInstanceOf[Double] - 0.5D) * 0.8D)
            j = MathHelper.floor_double(this.posZ + ((l / 2).asInstanceOf[Double] - 0.5D) * 0.8D);
            {
              var j1: Int = 0
              while (j1 < 2) {
                {
                  val k: Int = MathHelper.floor_double(this.posY) + j1
                  val block: Block = this.worldObj.getBlock(i1, k, j)
                  if (block eq Blocks.snow_layer) {
                    this.worldObj.setBlockToAir(i1, k, j)
                    this.isCollidedHorizontally = false
                  }
                  else if (block eq Blocks.waterlily) {
                    this.worldObj.func_147480_a(i1, k, j, true)
                    this.isCollidedHorizontally = false
                  }
                }
                {j1 += 1; j1}
              }
            }
          }
          {l += 1; l}
        }
      }
      /*
      if (this.onGround) {
        this.motionX *= 0.5D
        this.motionY *= 0.5D
        this.motionZ *= 0.5D
      }
wwww      */
      this.moveEntity(this.motionX, this.motionY, this.motionZ)
      if (this.isCollidedHorizontally && d10 > 0.2D) {
        if (!this.worldObj.isRemote && !this.isDead) {
          this.setDead();
          {
            l = 0
            while (l < 3) {
              {
                this.func_145778_a(Item.getItemFromBlock(Blocks.planks), 1, 0.0F)
              }
              {l += 1; l}
            }
          }
          {
            l = 0
            while (l < 2) {
              {
                this.func_145778_a(Items.stick, 1, 0.0F)
              }
              {l += 1; l}
            }
          }
        }
      }
      else {
        this.motionX *= 0.9900000095367432D
        this.motionY *= 0.949999988079071D
        this.motionZ *= 0.9900000095367432D
      }
      this.rotationPitch = 0.0F
      d4 = this.rotationYaw.asInstanceOf[Double]
      d11 = this.prevPosX - this.posX
      d12 = this.prevPosZ - this.posZ
      if (d11 * d11 + d12 * d12 > 0.001D) {
        d4 = (Math.atan2(d12, d11) * 180.0D / Math.PI).asInstanceOf[Float].asInstanceOf[Double]
      }
      var d7: Double = MathHelper.wrapAngleTo180_double(d4 - this.rotationYaw.asInstanceOf[Double])
      if (d7 > 20.0D) {
        d7 = 20.0D
      }
      if (d7 < -20.0D) {
        d7 = -20.0D
      }
      this.rotationYaw = (this.rotationYaw.asInstanceOf[Double] + d7).asInstanceOf[Float]
      setRotationPublic(this.rotationYaw, this.rotationPitch)
      if (!this.worldObj.isRemote) {
        val list: util.List[_] = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D))
        if (list != null && !list.isEmpty) {
          {
            var k1: Int = 0
            while (k1 < list.size) {
              {
                val entity: Entity = list.get(k1).asInstanceOf[Entity]
                if (entity != this.riddenByEntity && entity.canBePushed && entity.isInstanceOf[EntityBoat]) {
                  entity.applyEntityCollision(this)
                }
              }
              {k1 += 1; k1}
            }
          }
        }
        if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
          this.riddenByEntity = null
        }
      }
    }
  }

  /**
   * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
   */
  @SideOnly(Side.CLIENT) override def performHurtAnimation() {
    this.setForwardDirection(-this.getForwardDirection)
    this.setTimeSinceHit(10)
    this.setDamageTaken(this.getDamageTaken * 11.0F)
  }

  /**
   * Sets the damage taken from the last hit.
   */
  def setDamageTaken(p_70266_1_ : Float) {
    this.getDataWatcher.updateObject(19, Float.box(p_70266_1_))
  }

  /**
   * Gets the damage taken from the last hit.
   */
  def getDamageTaken: Float = {
    this.getDataWatcher.getWatchableObjectFloat(19)
  }

  /**
   * Sets the time to count down from since the last time entity was hit.
   */
  def setTimeSinceHit(p_70265_1_ : Int) {
    this.getDataWatcher.updateObject(17, Integer.valueOf(p_70265_1_))
  }

  /**
   * Gets the time since the last hit.
   */
  def getTimeSinceHit: Int = {
    this.getDataWatcher.getWatchableObjectInt(17)
  }

  /**
   * Sets the forward direction of the entity.
   */
  def setForwardDirection(p_70269_1_ : Int) {
    this.getDataWatcher.updateObject(18, Integer.valueOf(p_70269_1_))
  }

  /**
   * Gets the forward direction of the entity.
   */
  def getForwardDirection: Int = {
    this.getDataWatcher.getWatchableObjectInt(18)
  }
}