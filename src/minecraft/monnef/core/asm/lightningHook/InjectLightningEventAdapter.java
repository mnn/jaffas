package monnef.core.asm.lightningHook;

import monnef.core.asm.CoreTransformer;
import monnef.core.asm.ObfuscationHelper;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static monnef.core.MonnefCorePlugin.Log;
import static monnef.core.asm.MappedObject.C_WORLD;
import static monnef.core.asm.MappedObject.M_CAN_LIGHTNING_STRIKE_AT;
import static monnef.core.asm.lightningHook.InjectLightningEventAdapter.State.DONE;
import static monnef.core.asm.lightningHook.InjectLightningEventAdapter.State.LOOKING;
import static monnef.core.asm.lightningHook.InjectLightningEventAdapter.State.READ_CANSTRIKE;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IF_ACMPEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;

public class InjectLightningEventAdapter extends MethodVisitor {
    private Label savedLabel;

    enum State {LOOKING, READ_CANSTRIKE, DONE}

    public State getState() {
        return state;
    }

    private State state;

    public InjectLightningEventAdapter(MethodVisitor visitor) {
        super(ASM4, visitor);
        state = LOOKING;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        switch (state) {
            case LOOKING:
                if (opcode == INVOKEVIRTUAL && ObfuscationHelper.namesAreEqual(name, M_CAN_LIGHTNING_STRIKE_AT)) {
                    Log.printFine("Found canLightningStrikeAt method.");
                    state = READ_CANSTRIKE;
                }
                break;

            case READ_CANSTRIKE:
                state = LOOKING;
                break;

            case DONE:
                break;
        }

        mv.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        boolean insertHook = false;

        switch (state) {
            case LOOKING:
            case DONE:
                break;

            case READ_CANSTRIKE:
                if (opcode == IFEQ) {
                    savedLabel = label;
                    insertHook = true;
                    state = DONE;
                } else {
                    state = LOOKING;
                }
                break;
        }

        mv.visitJumpInsn(opcode, label);
        if (insertHook) insertHook();
    }

    private void insertHook() {
        mv.visitTypeInsn(NEW, "monnef/core/event/LightningGeneratedEvent");
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ILOAD, 11);
        mv.visitVarInsn(ILOAD, 13);
        mv.visitVarInsn(ILOAD, 12);
        mv.visitMethodInsn(INVOKESPECIAL, "monnef/core/event/LightningGeneratedEvent", "<init>", "(L" + ObfuscationHelper.getRealNameSlashed(C_WORLD) + ";III)V");
        mv.visitVarInsn(ASTORE, 14);
        mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/event/EventBus;");
        mv.visitVarInsn(ALOAD, 14);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/event/EventBus", "post", "(Lnet/minecraftforge/event/Event;)Z");
        mv.visitInsn(POP);
        mv.visitVarInsn(ALOAD, 14);
        mv.visitMethodInsn(INVOKEVIRTUAL, "monnef/core/event/LightningGeneratedEvent", "getResult", "()Lnet/minecraftforge/event/Event$Result;");
        mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/event/Event$Result", "DENY", "Lnet/minecraftforge/event/Event$Result;");
        mv.visitJumpInsn(IF_ACMPEQ, savedLabel);
        Log.printFine("Lightning hook inserted.");
        CoreTransformer.lightningHookApplied = true;
    }
}
