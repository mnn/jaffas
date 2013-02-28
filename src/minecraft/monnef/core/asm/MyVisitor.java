package monnef.core.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static monnef.core.asm.ObfuscationHelper.MappedObject.M_OBTAIN_ENTITY_SKIN;
import static monnef.core.asm.ObfuscationHelper.getRealName;

public class MyVisitor extends ClassVisitor {
    public MyVisitor(int version, ClassVisitor nextNode) {
        super(version, nextNode);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && getRealName(M_OBTAIN_ENTITY_SKIN).equals(name)) {
            mv = new InjectCloakHookAdapter(mv);
        }

        return mv;
    }
}
