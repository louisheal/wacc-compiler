package assembly;

import assembly.instructions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static assembly.instructions.Directive.DirectiveType.*;

public class LibraryFunctions {

    /* Function return registers. */
    private final static Register r0 = new Register(0);
    private final static Register r1 = new Register(1);

    /* General Registers. */
    private final static Register r4 = new Register(4);
    private final static Register r5 = new Register(5);
    private final static Register r6 = new Register(6);
    private final static Register r7 = new Register(7);
    private final static Register r8 = new Register(8);
    private final static Register r9 = new Register(9);
    private final static Register r10 = new Register(10);
    private final static Register r11 = new Register(11);

    /* Special registers. */
    private final static Register sp = new Register(13);
    private final static Register pc = new Register(15);

    public enum LFunctions {
        ONE
    }

    public static List<Instruction> getInstructions(Set<LFunctions> functions) {

        List<Instruction> instructions = new ArrayList<>();

        for (LFunctions function : functions) {
            instructions.addAll(getFunctionInstructions(function));
        }

        return instructions;
    }

    private static List<Instruction> getFunctionInstructions(LFunctions function) {
        switch (function) {
            case ONE:
        }
        return List.of();
    }

    private static List<Instruction> oneInstructions() {
        List<Instruction> instructions = new ArrayList<>();

        instructions.add(new LABEL("f_one:"));
        instructions.add(new LABEL("PUSH {lr}"));
        instructions.add(new LDR(r4, 1));
        instructions.add(new MOV(r0, new Operand2(r4)));
        instructions.add(new LABEL("POP {pc}"));
        instructions.add(new LABEL("POP {pc}"));
        instructions.add(new Directive(LTORG));

        return instructions;
    }

}
