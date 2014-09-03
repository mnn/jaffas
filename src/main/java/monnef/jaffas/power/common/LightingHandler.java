/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import monnef.core.MonnefCorePlugin;
import monnef.core.api.IIntegerCoordinates;
import monnef.core.event.LightningGeneratedEvent;
import monnef.core.utils.IntegerCoordinates;
import monnef.core.utils.WorldHelper;
import monnef.jaffas.power.JaffasPower;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.power.JaffasPower.lightningConductorRadius;

public class LightingHandler {
    @SubscribeEvent
    public void HandleLightning(LightningGeneratedEvent event) {
        int x = event.x;
        int y = event.y;
        int z = event.z;
        World w = event.world;
        Block conductor = JaffasPower.lightningConductor;

        Log.printDebug(String.format("Lightning detected at %d, %d, %d.", x, y, z));

        Block blockUnderBoltTarget = w.getBlock(x, y - 1, z);
        if (blockUnderBoltTarget == conductor) {
            Log.printDebug("Conductor detected, skipping.");
            return;
        }

        if (MonnefCorePlugin.debugEnv) {
            // TODO remove debug this code
            w.setBlock(x, y, z, Blocks.mycelium);
        }

        List<IIntegerCoordinates> blockInBox = new ArrayList<IIntegerCoordinates>();
        WorldHelper.getBlocksInBox(blockInBox, w, x, y, z, lightningConductorRadius, -1, -1, conductor, -1);
        if (blockInBox.size() == 0) return;

        ArrayList<IIntegerCoordinates> blocksInBoxStrikeAble = new ArrayList<IIntegerCoordinates>();
        for (IIntegerCoordinates item : blockInBox) {
            if (w.canLightningStrikeAt(item.getX(), item.getY() + 1, item.getZ())) {
                blocksInBoxStrikeAble.add(item);
            }
        }
        if (blocksInBoxStrikeAble.size() == 0) return;

        IntegerCoordinates boltOrigin = new IntegerCoordinates(x, WorldHelper.WORLD_HEIGHT, z, event.world);
        List<Float> distances = new ArrayList<Float>();
        float bestValue = Float.MAX_VALUE;
        int bestIndex = -1;
        for (int i = 0; i < blockInBox.size(); i++) {
            float dist = boltOrigin.computeDistanceSquare(blockInBox.get(i));
            if (dist < bestValue) {
                bestValue = dist;
                bestIndex = i;
            }
            distances.add(dist);
        }

        event.setResult(Event.Result.DENY);
        IIntegerCoordinates best = blocksInBoxStrikeAble.get(bestIndex);
        w.addWeatherEffect(new EntityLightningBolt(w, best.getX(), best.getY() + 1, best.getZ()));
        Log.printDebug(String.format("Conductor made his work. %d, %d, %d -> %d, %d, %d @ %g", x, y, z, best.getX(), best.getY() + 1, best.getZ(), bestValue));

        if (MonnefCorePlugin.debugEnv) {
            // TODO remove debug this code
            w.setBlock(x, y, z, Blocks.ice);
        }
    }
}
