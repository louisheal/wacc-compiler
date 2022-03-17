package assembly;

import assembly.instructions.*;
import ast.Param;
import ast.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static assembly.LibraryFunctions.LFunctions.*;
import static assembly.PredefinedFunctions.Functions;
import static assembly.instructions.Directive.DirectiveType.*;
import static ast.Type.EType.*;

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
    private final static Register lr = new Register(14);
    private final static Register pc = new Register(15);

    private static List<Param> getMaxParams() {
        List<Param> params = new ArrayList<>();
        params.add(new Param(new Type(ARRAY, new Type(INT)), "a"));
        return params;
    }

    private static List<Param> getMinParams() {
        List<Param> params = new ArrayList<>();
        params.add(new Param(new Type(ARRAY, new Type(INT)), "a"));
        return params;
    }

    public enum LFunctions {
        MAX("array_int_f_max", getMaxParams(), new Type(INT)),
        MIN("array_int_f_min", getMinParams(), new Type(INT));

        private final String ident;
        private final List<Param> params;
        private final Type returnType;

        LFunctions(String ident, List<Param> params, Type returnType) {
            this.ident = ident;
            this.params = params;
            this.returnType = returnType;
        }

        public String getIdent() {
            return ident;
        }

        public List<Param> getParams() {
            return params;
        }

        public Type getReturnType() {
            return returnType;
        }
    }

    public static Boolean isLibraryFunction(String ident) {
        for (LFunctions function : LFunctions.values()) {
            if (function.ident.equals(ident)) {
                return true;
            }
        }
        return false;
    }

    public static LFunctions getLibraryFunction(String ident) {
        for (LFunctions function : LFunctions.values()) {
            if (function.ident.equals(ident)) {
                return function;
            }
        }
        return null;
    }

    public static List<Instruction> getInstructions(Set<LFunctions> functions, Set<Functions> preFunctions) {

        List<Instruction> instructions = new ArrayList<>();

        for (LFunctions function : functions) {
            instructions.addAll(getFunctionInstructions(function, preFunctions));
        }

        return instructions;
    }

    private static List<Instruction> getFunctionInstructions(LFunctions function, Set<Functions> preFunctions) {
        switch (function) {
            case MAX:
                preFunctions.add(Functions.P_CHECK_ARRAY_BOUNDS);
                preFunctions.add(Functions.P_THROW_OVERFLOW_ERROR);
                return maxInstructions();
            case MIN:
                preFunctions.add(Functions.P_CHECK_ARRAY_BOUNDS);
                preFunctions.add(Functions.P_THROW_OVERFLOW_ERROR);
                return minInstructions();
            default:
                return new ArrayList<>();
        }
    }

    private static List<Instruction> maxInstructions() {
        List<Instruction> instructions = new ArrayList<>();

        instructions.add(new LABEL(MAX.getIdent() + ":"));
        instructions.add(new PUSH(lr));
        instructions.add(new SUB(sp, sp, new Operand2(8)));
        instructions.add(new LDR(r4, 1));
        instructions.add(new STR(r4, new Operand2(sp, 4)));
        instructions.add(new ADD(r4, sp, new Operand2(12)));
        instructions.add(new LDR(r5, 0));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new MOV(r0, new Operand2(r5)));
        instructions.add(new MOV(r1, new Operand2(r4)));
        instructions.add(new Branch("p_check_array_bounds").setSuffix("L"));
        instructions.add(new ADD(r4, r4, new Operand2(4)));
        instructions.add(new ADD(r4, r4, new Operand2(r5), 2));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new STR(r4, new Operand2(sp)));
        instructions.add(new Branch("L0_" + MAX.getIdent()));

        instructions.add(new LABEL("L1_" + MAX.getIdent() + ":"));
        instructions.add(new ADD(r4, sp, new Operand2(12)));
        instructions.add(new LDR(r5, new Operand2(sp, 4)));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new MOV(r0, new Operand2(r5)));
        instructions.add(new MOV(r1, new Operand2(r4)));
        instructions.add(new Branch("p_check_array_bounds").setSuffix("L"));
        instructions.add(new ADD(r4, r4, new Operand2(4)));
        instructions.add(new ADD(r4, r4, new Operand2(r5), 2));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new LDR(r5, new Operand2(sp)));
        instructions.add(new CMP(r4, new Operand2(r5)));
        instructions.add(new MOV(r4, 1, Conditionals.GT));
        instructions.add(new MOV(r4, 0, Conditionals.LE));
        instructions.add(new CMP(r4, 0));
        instructions.add(new Branch("L2_" + MAX.getIdent(), Conditionals.EQ));
        instructions.add(new ADD(r4, sp, new Operand2(12)));
        instructions.add(new LDR(r5, new Operand2(sp, 4)));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new MOV(r0, new Operand2(r5)));
        instructions.add(new MOV(r1, new Operand2(r4)));
        instructions.add(new Branch("p_check_array_bounds").setSuffix("L"));
        instructions.add(new ADD(r4, r4, new Operand2(4)));
        instructions.add(new ADD(r4, r4, new Operand2(r5), 2));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new STR(r4, new Operand2(sp)));
        instructions.add(new Branch("L3_" + MAX.getIdent()));

        instructions.add(new LABEL("L2_" + MAX.getIdent() + ":"));

        instructions.add(new LABEL("L3_" + MAX.getIdent() + ":"));
        instructions.add(new LDR(r4, new Operand2(sp, 4)));
        instructions.add(new LDR(r5, 1));
        instructions.add(new ADD(r4, r4, new Operand2(r5), Flags.S));
        instructions.add(new Branch("p_throw_overflow_error", Conditionals.VS).setSuffix("L"));
        instructions.add(new STR(r4, new Operand2(sp, 4)));

        instructions.add(new LABEL("L0_" + MAX.getIdent() + ":"));
        instructions.add(new LDR(r4, new Operand2(sp, 4)));
        instructions.add(new LDR(r5, new Operand2(sp, 12)));
        instructions.add(new LDR(r5, new Operand2(r5)));
        instructions.add(new CMP(r4, new Operand2(r5)));
        instructions.add(new MOV(r4, 1, Conditionals.LT));
        instructions.add(new MOV(r4, 0, Conditionals.GE));
        instructions.add(new CMP(r4, 1));
        instructions.add(new Branch("L1_" + MAX.getIdent(), Conditionals.EQ));
        instructions.add(new LDR(r4, new Operand2(sp)));
        instructions.add(new MOV(r0, new Operand2(r4)));
        instructions.add(new ADD(sp, sp, new Operand2(8)));

        instructions.add(new POP(pc));
        instructions.add(new POP(pc));
        instructions.add(new Directive(LTORG));

        return instructions;
    }

    private static List<Instruction> minInstructions() {
        List<Instruction> instructions = new ArrayList<>();

        instructions.add(new LABEL(MIN.getIdent() + ":"));
        instructions.add(new PUSH(lr));
        instructions.add(new SUB(sp, sp, new Operand2(8)));
        instructions.add(new LDR(r4, 1));
        instructions.add(new STR(r4, new Operand2(sp, 4)));
        instructions.add(new ADD(r4, sp, new Operand2(12)));
        instructions.add(new LDR(r5, 0));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new MOV(r0, new Operand2(r5)));
        instructions.add(new MOV(r1, new Operand2(r4)));
        instructions.add(new Branch("p_check_array_bounds").setSuffix("L"));
        instructions.add(new ADD(r4, r4, new Operand2(4)));
        instructions.add(new ADD(r4, r4, new Operand2(r5), 2));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new STR(r4, new Operand2(sp)));
        instructions.add(new Branch("L0_" + MIN.getIdent()));

        instructions.add(new LABEL("L1_" + MIN.getIdent() + ":"));
        instructions.add(new ADD(r4, sp, new Operand2(12)));
        instructions.add(new LDR(r5, new Operand2(sp, 4)));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new MOV(r0, new Operand2(r5)));
        instructions.add(new MOV(r1, new Operand2(r4)));
        instructions.add(new Branch("p_check_array_bounds").setSuffix("L"));
        instructions.add(new ADD(r4, r4, new Operand2(4)));
        instructions.add(new ADD(r4, r4, new Operand2(r5), 2));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new LDR(r5, new Operand2(sp)));
        instructions.add(new CMP(r4, new Operand2(r5)));
        instructions.add(new MOV(r4, 1, Conditionals.LE));
        instructions.add(new MOV(r4, 0, Conditionals.GT));
        instructions.add(new CMP(r4, 0));
        instructions.add(new Branch("L2_" + MIN.getIdent(), Conditionals.EQ));
        instructions.add(new ADD(r4, sp, new Operand2(12)));
        instructions.add(new LDR(r5, new Operand2(sp, 4)));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new MOV(r0, new Operand2(r5)));
        instructions.add(new MOV(r1, new Operand2(r4)));
        instructions.add(new Branch("p_check_array_bounds").setSuffix("L"));
        instructions.add(new ADD(r4, r4, new Operand2(4)));
        instructions.add(new ADD(r4, r4, new Operand2(r5), 2));
        instructions.add(new LDR(r4, new Operand2(r4)));
        instructions.add(new STR(r4, new Operand2(sp)));
        instructions.add(new Branch("L3_" + MIN.getIdent()));

        instructions.add(new LABEL("L2_" + MIN.getIdent() + ":"));

        instructions.add(new LABEL("L3_" + MIN.getIdent() + ":"));
        instructions.add(new LDR(r4, new Operand2(sp, 4)));
        instructions.add(new LDR(r5, 1));
        instructions.add(new ADD(r4, r4, new Operand2(r5), Flags.S));
        instructions.add(new Branch("p_throw_overflow_error", Conditionals.VS).setSuffix("L"));
        instructions.add(new STR(r4, new Operand2(sp, 4)));

        instructions.add(new LABEL("L0_" + MIN.getIdent() + ":"));
        instructions.add(new LDR(r4, new Operand2(sp, 4)));
        instructions.add(new LDR(r5, new Operand2(sp, 12)));
        instructions.add(new LDR(r5, new Operand2(r5)));
        instructions.add(new CMP(r4, new Operand2(r5)));
        instructions.add(new MOV(r4, 1, Conditionals.LT));
        instructions.add(new MOV(r4, 0, Conditionals.GE));
        instructions.add(new CMP(r4, 1));
        instructions.add(new Branch("L1_" + MIN.getIdent(), Conditionals.EQ));
        instructions.add(new LDR(r4, new Operand2(sp)));
        instructions.add(new MOV(r0, new Operand2(r4)));
        instructions.add(new ADD(sp, sp, new Operand2(8)));

        instructions.add(new POP(pc));
        instructions.add(new POP(pc));
        instructions.add(new Directive(LTORG));

        return instructions;
    }

}
