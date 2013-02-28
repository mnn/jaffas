package monnef.core.asm;

import cpw.mods.fml.relauncher.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM4;

public class CoreTransformer implements IClassTransformer {
    public static boolean cloakHookApplied = false;

    @Override
    public byte[] transform(String name, byte[] bytes) {
        if (bytes == null) return null;

        if ("net.minecraft.client.renderer.RenderGlobal".equals(name)) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassReader reader = new ClassReader(bytes);
            ClassVisitor visitor = new MyVisitor(ASM4, writer);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }

        return bytes;
    }
}

