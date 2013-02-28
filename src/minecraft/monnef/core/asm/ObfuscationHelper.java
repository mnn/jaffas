package monnef.core.asm;

import monnef.core.MonnefCorePlugin;

import static monnef.core.asm.ObfuscationHelper.MappedObjectType.CLASS;
import static monnef.core.asm.ObfuscationHelper.MappedObjectType.METHOD;

public class ObfuscationHelper {
    private static boolean runningInObfuscatedMode = !MonnefCorePlugin.debugEnv;

    public static String getRealName(MappedObject toTranslate) {
        if (!runningInObfuscatedMode) {
            return toTranslate.getFullName();
        }

        // TODO translation
        return "TODO";
    }

    enum MappedObjectType {
        CLASS, FIELD, METHOD
    }

    enum MappedObject {
        C_ENTITY("net/minecraft/entity/Entity", CLASS),
        M_UPDATE_CLOAK("updateCloak", METHOD),
        M_OBTAIN_ENTITY_SKIN("obtainEntitySkin", METHOD),
        M_RENDER_GLOBAL("net.minecraft.client.renderer.RenderGlobal", METHOD);

        private final String fullName;
        private final MappedObjectType type;

        MappedObject(String fullName, MappedObjectType type) {
            this.fullName = fullName;
            this.type = type;
        }

        public String getFullName() {
            return fullName;
        }

        public MappedObjectType getType() {
            return type;
        }
    }
}
