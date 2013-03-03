package monnef.core.event;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;

@Event.HasResult
public class LightningGeneratedEvent extends Event {
    public final int x;
    public final int y;
    public final int z;
    public final World world;

    public LightningGeneratedEvent(World world, int x, int y, int z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }
}
