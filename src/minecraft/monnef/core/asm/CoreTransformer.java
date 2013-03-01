package monnef.core.asm;

import cpw.mods.fml.relauncher.IClassTransformer;
import monnef.core.MonnefCorePlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static monnef.core.asm.MappedObject.C_RENDER_GLOBAL;
import static org.objectweb.asm.Opcodes.ASM4;

public class CoreTransformer implements IClassTransformer {
    public static boolean cloakHookApplied = false;

    public CoreTransformer() {
    }

    @Override
    public byte[] transform(String name, byte[] bytes) {
        if (ObfuscationHelper.cl == null) {
            ObfuscationHelper.cl = getClass().getClassLoader();
        }

        if (bytes == null) return null;

        if (ObfuscationHelper.namesAreEqual(name, C_RENDER_GLOBAL)) {
            MonnefCorePlugin.Log.printFine("Found RenderGlobal class.");
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassReader reader = new ClassReader(bytes);
            ClassVisitor visitor = new MyVisitor(ASM4, writer);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }

        return bytes;
    }
}

