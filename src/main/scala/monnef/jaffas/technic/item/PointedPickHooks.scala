package monnef.jaffas.technic.item

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.event.entity.living.{LivingDeathEvent, LivingHurtEvent}
import monnef.core.utils.{PlayerHelper, DamageSourceHelper}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.monster.{EntityZombie, EntityCreeper, EntityEnderman}
import net.minecraft.item.Item
import monnef.jaffas.technic.JaffasTechnic
import net.minecraft.entity.EntityLivingBase
import monnef.core.utils.DamageSourceHelper.LivingHurtAndDeathEventWrapper
import scala.util.Random

class PointedPickHooks {
  @SubscribeEvent
  def entityHurt(event: LivingHurtEvent) {
    DamageSourceHelper.handleDamageInflictedByToolAndPlayer(
      event,
      getPointedPick,
      12,
      e => e.isInstanceOf[EntityEnderman] || e.isInstanceOf[EntityCreeper],
      (player, mob, event) => {}
    )
  }

  @SubscribeEvent
  def entityDeath(event: LivingDeathEvent) {
    val handleDeathWithRightToolAndMob = (player: EntityPlayer, mob: EntityLivingBase, event: LivingDeathEvent) => {
      PlayerHelper.damageCurrentItem(player)
      if (!player.worldObj.isRemote) {
        if (Random.nextInt(3) == 0) {
          val c = new EntityEnderman(mob.worldObj)
          c.setPosition(mob.posX, mob.posY, mob.posZ)
          c.setScreaming(true)
          c.setTarget(player)
          player.worldObj.spawnEntityInWorld(c)
        }
      }
    }: Unit

    DamageSourceHelper.handlePlayerHurtingMobWithItem(
      new LivingHurtAndDeathEventWrapper(event),
      getPointedPick,
      e => e.isInstanceOf[EntityCreeper] || e.isInstanceOf[EntityZombie],
      handleDeathWithRightToolAndMob
    )
  }

  private def getPointedPick: Item = JaffasTechnic.pointedPick
}
