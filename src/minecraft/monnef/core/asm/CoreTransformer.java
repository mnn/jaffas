package monnef.core.asm;

import cpw.mods.fml.relauncher.IClassTransformer;
import monnef.core.asm.cloakHook.RenderGlobalVisitor;
import monnef.core.asm.lightningHook.WorldServerVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static monnef.core.MonnefCorePlugin.Log;
import static monnef.core.asm.MappedObject.C_RENDER_GLOBAL;
import static monnef.core.asm.MappedObject.C_WORLD_SERVER;
import static org.objectweb.asm.Opcodes.ASM4;

public class CoreTransformer implements IClassTransformer {
    public static boolean cloakHookApplied = false;
    public static boolean lightningHookApplied = false;

    public CoreTransformer() {
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;

        if (ObfuscationHelper.namesAreEqual(name, C_RENDER_GLOBAL)) {
            Log.printFine("Found RenderGlobal class.");
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassReader reader = new ClassReader(bytes);
            ClassVisitor visitor = new RenderGlobalVisitor(ASM4, writer);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        } else if (ObfuscationHelper.namesAreEqual(name, C_WORLD_SERVER)) {
            Log.printFine("Found WorldServer class.");
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassReader reader = new ClassReader(bytes);
            ClassVisitor visitor = new WorldServerVisitor(ASM4, writer);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }

        return bytes;
    }
}

