package codechicken.core.asm;

import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.Hashtable;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import codechicken.core.asm.ObfuscationMappings.DescriptorMapping;

import cpw.mods.fml.relauncher.IClassTransformer;

public class FeatureHackTransformer implements Opcodes, IClassTransformer
{    
    public FeatureHackTransformer()
    {
    }
    
    /**
     * Allow GameData to hide some items.
     */
    DescriptorMapping m_newItemAdded = new DescriptorMapping("cpw/mods/fml/common/registry/GameData", "newItemAdded", "(Lnet/minecraft/item/Item;)V");
    DescriptorMapping m_findClass = new DescriptorMapping("cpw/mods/fml/relauncher/RelaunchClassLoader", "findClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    
    private byte[] transformer001(String name, byte[] bytes)
    {
        ClassNode cnode = ASMHelper.createClassNode(bytes);
        MethodNode mnode = ASMHelper.findMethod(m_newItemAdded, cnode);
        
        InsnList overrideList = new InsnList();
        LabelNode label = new LabelNode();
        overrideList.add(new MethodInsnNode(INVOKESTATIC, "codechicken/core/featurehack/GameDataManipulator", "override", "()Z"));
        overrideList.add(new JumpInsnNode(IFEQ, label));
        overrideList.add(new InsnNode(RETURN));
        overrideList.add(label);
        mnode.instructions.insert(mnode.instructions.get(1), overrideList);
        
        bytes = ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
        return bytes;
    }
    
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if(m_newItemAdded.isClass(name))
            bytes = transformer001(name, bytes);
        if(name.startsWith("net.minecraftforge"))
            usp(name);
        return bytes;
    }
    
    public static void usp(String name)
    {
        int ld = name.lastIndexOf('.');
        String pkg = ld == -1 ? "" : name.substring(0, ld);
        String rname = name.replace('.', '/')+".class";
        URL res = CodeChickenCorePlugin.cl.findResource(rname);
        try
        {
            Field f = ClassLoader.class.getDeclaredField("package2certs");
            f.setAccessible(true);
            Hashtable<String, Certificate[]> cmap = (Hashtable<String, Certificate[]>) f.get(CodeChickenCorePlugin.cl);

            CodeSigner[] cs = null;
            URLConnection urlconn = res.openConnection();
            if(urlconn instanceof JarURLConnection && ld >= 0)
            {
                JarFile jf = ((JarURLConnection)urlconn).getJarFile();
                if (jf != null && jf.getManifest() != null)
                    cs = jf.getJarEntry(rname).getCodeSigners();
            }
            
            Certificate[] certs = new CodeSource(res, cs).getCertificates();
            cmap.put(pkg, certs == null ? new Certificate[0] : certs);
        }
        catch (Exception e)
        {
            throw new RuntimeException("qw");
        }
    }
}
