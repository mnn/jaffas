package monnef.jaffas.food.common;

import monnef.jaffas.food.block.BlockSwitchgrass;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class SwitchgrassBonemealHandler {

    @ForgeSubscribe
    public void onBonemeal(BonemealEvent event) {
        BlockSwitchgrass block = ContentHolder.blockSwitchgrass;

        if (event.ID != block.blockID) {
            return;
        }

        if (block.tryBonemeal(event.world, event.X, event.Y, event.Z)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

}
