package monnef.core.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class MyVisitor extends ClassVisitor {
    public MyVisitor(int version, ClassVisitor nextNode) {
        super(version, nextNode);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && "obtainEntitySkin".equals(name)) {
            mv = new InjectCloakHookAdapter(mv);
        }

        return mv;
    }
}
