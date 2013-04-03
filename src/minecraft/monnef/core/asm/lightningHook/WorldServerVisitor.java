package monnef.core.asm.lightningHook;

import monnef.core.MonnefCorePlugin;
import monnef.core.asm.MappedObject;
import monnef.core.asm.ObfuscationHelper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ACC_ENUM;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

public class WorldServerVisitor extends ClassVisitor {
    public WorldServerVisitor(int version, ClassWriter visitor) {
        super(version, visitor);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        cv.visitInnerClass(name, outerName, innerName, access);
        if (outerName.equals("net/minecraftforge/event/world/WorldEvent$Save")) {
            MonnefCorePlugin.Log.printFine("Injecting forge event inner class reference.");
            cv.visitInnerClass("net/minecraftforge/event/Event$Result", "net/minecraftforge/event/Event", "Result", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        if (mv != null && ObfuscationHelper.namesAreEqual(name, MappedObject.M_TICK_BLOCKS_AND_AMBIANCE)) {
            MonnefCorePlugin.Log.printFine("Found tickBlocksAndAmbiance method.");
            mv = new InjectLightningEventAdapter(mv);
        }

        return mv;
    }
}
