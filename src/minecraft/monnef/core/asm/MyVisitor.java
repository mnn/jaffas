package monnef.core.asm;

import monnef.core.MonnefCorePlugin;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static monnef.core.asm.MappedObject.M_OBTAIN_ENTITY_SKIN;

public class MyVisitor extends ClassVisitor {

    public MyVisitor(int version, ClassVisitor nextNode) {
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
