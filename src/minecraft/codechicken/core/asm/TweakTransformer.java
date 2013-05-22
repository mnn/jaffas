package codechicken.core.asm;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.HashMultimap;

import codechicken.core.asm.ASMHelper.MethodAltercator;
import codechicken.core.asm.InstructionComparator.InsnListSection;
import codechicken.core.asm.ObfuscationMappings.DescriptorMapping;
import codechicken.core.config.ConfigFile;
import codechicken.core.config.ConfigTag;
import cpw.mods.fml.relauncher.IClassTransformer;

public class TweakTransformer implements IClassTransformer, Opcodes
{
    private static HashMultimap<String, MethodAltercator> altercators = HashMultimap.create();    
    public static ConfigTag tweaks;

    public static void load(ConfigFile config)
    {
        tweaks = config.getTag("tweaks").setComment("Various tweaks that can be applied to game mechanics.").useBraces();
        
        if(!tweaks.getTag("persistantLava")
                .setComment("Set to false to make lava fade away like water if all the source blocks are destroyed")
                .getBooleanValue(true))
        {
            alterMethod(new MethodAltercator(new DescriptorMapping("net/minecraft/block/Block", "updateTick", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V").subclass("net/minecraft/block/BlockFlowing"))
            {
                @Override
                public void alter(MethodNode mv)
                {
                    InsnList needle = new InsnList();
                    needle.add(new VarInsnNode(ILOAD, 6));
                    needle.add(new VarInsnNode(ISTORE, -1));//9 in forge, 10 in vanilla
                    needle.add(new InsnNode(ICONST_0));
                    needle.add(new VarInsnNode(ISTORE, 8));
                    
                    List<InsnListSection> lists = InstructionComparator.insnListFindL(mv.instructions, needle);
                    if(lists.size() != 1)
                        throw new RuntimeException("Needle found "+lists.size()+" times in Haystack: " + mv.instructions+"\n" + ASMHelper.printInsnList(needle));

                    InsnListSection subsection = lists.get(0);
                    
                    AbstractInsnNode insn = subsection.first;
                    while(true)
                    {
                        AbstractInsnNode next = insn.getNext();
                        mv.instructions.remove(insn);
                        if(insn == subsection.last)
                            break;
                        insn = next;
                    }
                }
            });
        }
        
        if(tweaks.getTag("environmentallyFriendlyCreepers")
                .setComment("If set to true, creepers will not destroy landscape. (A version of mobGreifing setting just for creepers)")
                .getBooleanValue(false))
        {
            alterMethod(new MethodAltercator(new DescriptorMapping("net/minecraft/entity/Entity", "onUpdate", "()V").subclass("net/minecraft/entity/monster/EntityCreeper"))
            {
                @Override
                public void alter(MethodNode mv)
                {
                    InsnList needle = new InsnList();
                    needle.add(new VarInsnNode(ALOAD, 0));
                    needle.add(new DescriptorMapping("net/minecraft/entity/Entity", "worldObj", "Lnet/minecraft/world/World;").subclass("net/minecraft/entity/monster/EntityCreeper")
                            .toFieldInsn(GETFIELD));
                    needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "getGameRules", "()Lnet/minecraft/world/GameRules;"));
                    needle.add(new LdcInsnNode("mobGriefing"));
                    needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/GameRules", "getGameRuleBooleanValue", "(Ljava/lang/String;)Z"));
                                        
                    List<InsnListSection> lists = InstructionComparator.insnListFindL(mv.instructions, needle);
                    if(lists.size() != 1)
                        throw new RuntimeException("Needle found "+lists.size()+" times in Haystack: " + mv.instructions+"\n" + ASMHelper.printInsnList(needle));

                    InsnListSection subsection = lists.get(0);
                    
                    mv.instructions.insertBefore(subsection.first, new InsnNode(ICONST_0));
                    
                    AbstractInsnNode insn = subsection.first;
                    while(true)
                    {
                        AbstractInsnNode next = insn.getNext();
                        mv.instructions.remove(insn);
                        if(insn == subsection.last)
                            break;
                        insn = next;
                    }
                }
            });
        }
        
        if(!tweaks.getTag("softLeafReplace")
                .setComment("If set to false, leaves will only replace air when growing")
                .getBooleanValue(false))
        {
            alterMethod(new MethodAltercator(new DescriptorMapping("net/minecraft/block/Block", "canBeReplacedByLeaves", "(Lnet/minecraft/world/World;III)Z"))
            {
                @Override
                public void alter(MethodNode mv)
                {
                    InsnList replacement = new InsnList();
                    replacement.add(new VarInsnNode(ALOAD, 0));
                    replacement.add(new VarInsnNode(ALOAD, 1));
                    replacement.add(new VarInsnNode(ILOAD, 2));
                    replacement.add(new VarInsnNode(ILOAD, 3));
                    replacement.add(new VarInsnNode(ILOAD, 4));
                    replacement.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block", "isAirBlock", "(Lnet/minecraft/world/World;III)Z"));
                    replacement.add(new InsnNode(IRETURN));
                    
                    mv.instructions = replacement;
                }
            });
        }
        
        if(tweaks.getTag("doFireTickOut")
                .setComment("If set to true and doFireTick is disabed in the game rules, fire will still dissipate if it's not over a fire source")
                .getBooleanValue(true))
        {
            alterMethod(new MethodAltercator(new DescriptorMapping("net/minecraft/block/BlockFire", "updateTick", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V"))
            {
                @Override
                public void alter(MethodNode mv)
                {
                    InsnList needle = new InsnList();
                    needle.add(new LdcInsnNode("doFireTick"));
                    needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/GameRules", "getGameRuleBooleanValue", "(Ljava/lang/String;)Z"));
                    needle.add(new JumpInsnNode(IFEQ, new LabelNode()));
                    
                    List<InsnListSection> lists = InstructionComparator.insnListFindL(mv.instructions, needle);
                    if(lists.size() != 1)
                        throw new RuntimeException("Needle found "+lists.size()+" times in Haystack: " + mv.instructions+"\n" + ASMHelper.printInsnList(needle));
                    
                    InsnListSection subsection = lists.get(0);
                    LabelNode jlabel = ((JumpInsnNode)subsection.last).label;
                    LabelNode ret = new LabelNode();
                    mv.instructions.insertBefore(jlabel, new JumpInsnNode(GOTO, ret));
                    InsnList inject = new InsnList();
                    inject.add(new VarInsnNode(ALOAD, 1));
                    inject.add(new VarInsnNode(ILOAD, 2));
                    inject.add(new VarInsnNode(ILOAD, 3));
                    inject.add(new VarInsnNode(ILOAD, 4));
                    inject.add(new VarInsnNode(ALOAD, 5));
                    inject.add(new MethodInsnNode(INVOKESTATIC, "codechicken/core/featurehack/TweakTransformerHelper", "quenchFireTick", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V"));
                    inject.add(ret);
                    mv.instructions.insert(jlabel, inject);
                }
            });
        }
    }
    
    private static void alterMethod(MethodAltercator ma)
    {
        altercators.put(ma.method.javaClass(), ma);
    }
    
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        bytes = ASMHelper.alterMethods(name, bytes, altercators);
        return bytes;
    }
}
