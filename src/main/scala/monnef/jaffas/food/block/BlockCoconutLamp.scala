package monnef.jaffas.food.block

import net.minecraft.block.material.Material
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import cpw.mods.fml.relauncher.{SideOnly, Side}
import java.util.Random
import net.minecraft.tileentity.TileEntity
import net.minecraft.block.Block
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.entity.player.EntityPlayer
import monnef.core.utils.{scalautils, BlockHelper, PlayerHelper}
import monnef.core.MonnefCorePlugin
import monnef.jaffas.food.common.ContentHolder
import scalautils._

class BlockCoconutLamp(_texture: Int) extends BlockJaffas(_texture, Material.rock) {
  setHardness(0.1f)
  setLightLevel(.95f)
  setStepSound(Block.soundTypeWood)
  setBlockName("coconutLamp")
  setTickRandomly(true)

  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB = null

  override def isOpaqueCube: Boolean = false

  override def renderAsNormalBlock(): Boolean = false

  @SideOnly(Side.CLIENT) override def randomDisplayTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
    val xx = x + 0.5F
    val yy = y + 1 / 16f * 5
    val zz = z + 0.5F

    world.spawnParticle("smoke", xx, yy, zz, 0.0D, 0.0D, 0.0D)
    world.spawnParticle("flame", xx, yy, zz, 0.0D, 0.0D, 0.0D)
  }

  override def hasTileEntity(metadata: Int): Boolean = true

  override def createTileEntity(world: World, metadata: Int): TileEntity = new TileCoconutLamp

  override def getLightValue: Int = super.getLightValue

  override def onBlockPlaced(world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, meta: Int): Int = {
    ForgeDirection.getOrientation(side).getOpposite.ordinal()
  }

  override def onBlockClicked(world: World, x: Int, y: Int, z: Int, player: EntityPlayer) {
    if (MonnefCorePlugin.debugEnv) {
      val metadata = world.getBlockMetadata(x, y, z)
      PlayerHelper.addMessage(player, s"meta: $metadata -> side: ${ForgeDirection.getOrientation(metadata)}")
    }
    super.onBlockClicked(world, x, y, z, player)
  }

  override def getRenderType: Int = ContentHolder.renderID

  override def canBlockStay(world: World, x: Int, y: Int, z: Int): Boolean = world.getBlock(x, y - 1, z).getMaterial.isSolid

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    var drop = false
    if (!canBlockStay(world, x, y, z)) {
      drop = true
    }

    if (drop) {
      dropBlockAsItem(world, x, y, z, 0, 0)
      BlockHelper.setAir(world, x, y, z)
    }
  }

  override def updateTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
    super.updateTick(world, x, y, z, rand)
    val blockAbove = world.getBlock(x, y + 1, z)
    if (blockAbove.isFlammable(world, x, y + 1, z, ForgeDirection.DOWN) && rand.nextInt(5) == 0) {
      val toCheck = (x, y + 2, z) +: (for (xx <- Seq(-1, 1);zz <- Seq(-1, 1)) yield (x + xx, y + 1, z + zz))
      val candidates = toCheck.filter { case (xx, yy, zz) => world.isAirBlock(xx, yy, zz)}
      if (candidates.nonEmpty) candidates.random |> { case (xx, yy, zz) => BlockHelper.setFire(world, xx, yy, zz)}
    }
  }
}
