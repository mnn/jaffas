package monnef.jaffas.food.common;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import monnef.jaffas.food.block.BlockSwitchgrass;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class SwitchgrassBonemealHandler {

    @SubscribeEvent
    public void onBonemeal(BonemealEvent event) {
        BlockSwitchgrass block = ContentHolder.blockSwitchgrass;

        if (event.block != block) {
            return;
        }

        if (block.tryBonemeal(event.world, event.x, event.y, event.z)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

}
