/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.power.common;

import monnef.core.MonnefCorePlugin;
import monnef.core.event.LightningGeneratedEvent;
import monnef.core.utils.IntegerCoordinates;
import monnef.core.utils.WorldHelper;
import monnef.jaffas.power.JaffasPower;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;

import java.util.ArrayList;
import java.util.List;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.power.JaffasPower.lightningConductorRadius;

public class LightingHandler {
    @ForgeSubscribe
    public void HandleLightning(LightningGeneratedEvent event) {
        int x = event.x;
        int y = event.y;
        int z = event.z;
        World w = event.world;
        int conductorId = JaffasPower.lightningConductor.blockID;

        Log.printDebug(String.format("Lightning detected at %d, %d, %d.", x, y, z));

        int blockIdUnderBoltTarget = w.getBlockId(x, y - 1, z);
        if (blockIdUnderBoltTarget == conductorId) {
            Log.printDebug("Conductor detected, skipping.");
            return;
        }

        if (MonnefCorePlugin.debugEnv) {
            // TODO remove debug this code
            w.setBlock(x, y, z, Block.mycelium.blockID);
        }

        List<IntegerCoordinates> blockInBox = new ArrayList<IntegerCoordinates>();
        WorldHelper.getBlocksInBox(blockInBox, w, x, y, z, lightningConductorRadius, -1, -1, conductorId, -1);
        if (blockInBox.size() == 0) return;

        ArrayList<IntegerCoordinates> blocksInBoxStrikeAble = new ArrayList<IntegerCoordinates>();
        for (IntegerCoordinates item : blockInBox) {
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
        IntegerCoordinates best = blocksInBoxStrikeAble.get(bestIndex);
        w.addWeatherEffect(new EntityLightningBolt(w, best.getX(), best.getY() + 1, best.getZ()));
        Log.printDebug(String.format("Conductor made his work. %d, %d, %d -> %d, %d, %d @ %g", x, y, z, best.getX(), best.getY() + 1, best.getZ(), bestValue));

        if (MonnefCorePlugin.debugEnv) {
            // TODO remove debug this code
            w.setBlock(x, y, z, Block.ice.blockID);
        }
    }
}
