package assembly.instructions;

import assembly.Register;

public class BoolOp extends Instruction {
    
    private BoolOpType type;

    // AND dest, rn, rm
    // ORR dest, rn, rm
    public BoolOp(BoolOpType type, Register dest, Register rn, Register rm) {
        this.type = type;
        this.dest = dest;
        this.rn = rn;
        this.rm = rm;
    }
    
    public enum BoolOpType {
        AND,
        ORR
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s, %s", type, dest, rn, rm);
    }
}
