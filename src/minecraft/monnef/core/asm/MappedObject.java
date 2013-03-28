package monnef.core.asm;

import static monnef.core.asm.MappedObjectType.CLASS;
import static monnef.core.asm.MappedObjectType.METHOD;

public enum MappedObject {
    C_ENTITY("net.minecraft.entity.Entity", CLASS),
    M_UPDATE_CLOAK("updateCloak", METHOD),
    M_OBTAIN_ENTITY_SKIN("onEntityCreate", METHOD),
    C_RENDER_GLOBAL("net.minecraft.client.renderer.RenderGlobal", CLASS),
    C_WORLD_SERVER("net.minecraft.world.WorldServer", CLASS),
    M_TICK_BLOCKS_AND_AMBIANCE("tickBlocksAndAmbiance", METHOD),
    M_CAN_LIGHTNING_STRIKE_AT("canLightningStrikeAt", METHOD),
    C_WORLD("net.minecraft.world.World", CLASS);

    private final String fullName;
    private final MappedObjectType type;

    MappedObject(String fullName, MappedObjectType type) {
        this.fullName = fullName.replace('/', '.');
        this.type = type;
    }

    // always "dotted" form
    public String getFullName() {
        return fullName;
    }

    public MappedObjectType getType() {
        return type;
    }
}
