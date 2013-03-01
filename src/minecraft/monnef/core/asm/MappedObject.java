package monnef.core.asm;

import static monnef.core.asm.MappedObjectType.CLASS;
import static monnef.core.asm.MappedObjectType.METHOD;

public enum MappedObject {
    C_ENTITY("net.minecraft.entity.Entity", CLASS),
    M_UPDATE_CLOAK("updateCloak", METHOD),
    M_OBTAIN_ENTITY_SKIN("obtainEntitySkin", METHOD),
    C_RENDER_GLOBAL("net.minecraft.client.renderer.RenderGlobal", CLASS);

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
