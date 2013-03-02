package monnef.core.asm.cloakHook;

import monnef.core.asm.CoreTransformer;
import monnef.core.asm.ObfuscationHelper;
import org.objectweb.asm.MethodVisitor;

import static monnef.core.MonnefCorePlugin.Log;
import static monnef.core.asm.MappedObject.C_ENTITY;
import static monnef.core.asm.MappedObject.M_UPDATE_CLOAK;
import static monnef.core.asm.ObfuscationHelper.getRealNameSlashed;
import static org.objectweb.asm.Opcodes.*;

public class InjectCloakHookAdapter extends MethodVisitor {
    public State getState() {
        return state;
    }

    enum State {LOOKING, READ_UPDATECLOAK, DONE}

    private State state;

    public InjectCloakHookAdapter(MethodVisitor visitor) {
        super(ASM4, visitor);
        state = State.LOOKING;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        switch (state) {
            case LOOKING:
                if (opcode == INVOKEVIRTUAL && ObfuscationHelper.namesAreEqual(name, M_UPDATE_CLOAK)) {
                    Log.printFine("Found updateCloak method.");
                    state = State.READ_UPDATECLOAK;
                }
                break;

            case READ_UPDATECLOAK:
                state = State.LOOKING;
                break;

            case DONE:
                break;
        }

        mv.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        boolean insertHook = false;

        switch (state) {
            case LOOKING:
            case DONE:
                break;

            case READ_UPDATECLOAK:
                //if (opcode == ALOAD && var == 1) {
                if (opcode == ALOAD) {
                    insertHook = true;
                    state = State.DONE;
                } else {
                    state = State.LOOKING;
                }
                break;
        }

        mv.visitVarInsn(opcode, var);
        if (insertHook) insertHook();
    }

    private void insertHook() {
        String signature = "(L" + getRealNameSlashed(C_ENTITY) + ";)V";
        mv.visitMethodInsn(INVOKESTATIC, "monnef/core/CloakHookHandler", "handleUpdateCloak", signature);
        mv.visitVarInsn(ALOAD, 1);
        CoreTransformer.cloakHookApplied = true;
        Log.printFine("Cloak hook inserted.");
    }

}
