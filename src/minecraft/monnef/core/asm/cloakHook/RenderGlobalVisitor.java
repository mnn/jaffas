package monnef.core.asm.cloakHook;

import monnef.core.MonnefCorePlugin;
import monnef.core.asm.ObfuscationHelper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static monnef.core.asm.MappedObject.M_OBTAIN_ENTITY_SKIN;

public class RenderGlobalVisitor extends ClassVisitor {

    public RenderGlobalVisitor(int version, ClassVisitor nextNode) {
        super(version, nextNode);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && ObfuscationHelper.namesAreEqual(name, M_OBTAIN_ENTITY_SKIN)) {
            MonnefCorePlugin.Log.printFine("Found obtainEntitySkin method.");
            mv = new InjectCloakHookAdapter(mv);
        }

        return mv;
    }
}
