package assembly;

import assembly.instructions.*;
import assembly.instructions.BoolOp.BoolOpType;
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
    private static List<Param> getAbsParams() {
      List<Param> params = new ArrayList<>();
      params.add(new Param(new Type(INT), "x"));
      return params;
  }
    private static List<Param> getIsAlNumParams(){
      List<Param> params = new ArrayList<>();
      params.add(new Param(new Type(CHAR), "c"));
      return params;
    }

    private static List<Param> getIsAlphaParams(){
      List<Param> params = new ArrayList<>();
      params.add(new Param(new Type(CHAR), "c"));
      return params;
    }

  private static List<Param> getIsAsciiParams(){
    List<Param> params = new ArrayList<>();
    params.add(new Param(new Type(CHAR), "c"));
    return params;
  }
  private static List<Param> getIsDigitParams(){
    List<Param> params = new ArrayList<>();
    params.add(new Param(new Type(CHAR), "c"));
    return params;
  }

  private static List<Param> getIsUpperParams(){
    List<Param> params = new ArrayList<>();
    params.add(new Param(new Type(CHAR), "c"));
    return params;
  }

  private static List<Param> getIsLowerParams(){
    List<Param> params = new ArrayList<>();
    params.add(new Param(new Type(CHAR), "c"));
    return params;
  }

  private static List<Param> getIsSpaceParams(){
    List<Param> params = new ArrayList<>();
    params.add(new Param(new Type(CHAR), "c"));
    return params;
  }


    private static List<Param> getMinParams() {
        List<Param> params = new ArrayList<>();
        params.add(new Param(new Type(ARRAY, new Type(INT)), "a"));
        return params;
    }

    public enum LFunctions {
        MAX("array_int_f_max", getMaxParams(), new Type(INT)),

        ABS("int_f_abs", getAbsParams(), new Type(INT)),

        ISALNUM("char_f_isAlnum", getIsAlNumParams(), new Type(BOOL)),

        ISALPHA("char_f_isAlpha", getIsAlphaParams(), new Type(BOOL)),

        ISASCII("char_f_isAscii", getIsAsciiParams(), new Type(BOOL)),

        ISDIGIT("char_f_isDigit", getIsDigitParams(), new Type(BOOL)),

        ISUPPER("char_f_isUpper", getIsUpperParams(), new Type(BOOL)),

        ISLOWER("char_f_isLower", getIsLowerParams(), new Type(BOOL)),

        ISSPACE("char_f_isSpace", getIsSpaceParams(), new Type(BOOL)),

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
            case ABS:
                preFunctions.add(Functions.P_THROW_OVERFLOW_ERROR);
                return absInstructions();
            case MIN:
                preFunctions.add(Functions.P_CHECK_ARRAY_BOUNDS);
                preFunctions.add(Functions.P_THROW_OVERFLOW_ERROR);
                return minInstructions();
          case ISALNUM:
                return isAlnumInstructions();
          case ISALPHA:
                return isAlphaInstructions();
          case ISASCII:
                return isAsciiInstructions();
          case ISDIGIT:
                return isDigitInstructions();
          case ISLOWER:
              return isLowerInstructions();
          case ISUPPER:
            return  isUpperInstructions();
          case ISSPACE:
            return isSpaceInstructions();
            default:
                return new ArrayList<>();
        }
    }

    private static List<Instruction> absInstructions() {
      List<Instruction> instructions = new ArrayList<>();
      //int_f_abs:
      instructions.add(new LABEL("int_f_abs:"));

      // PUSH {lr}
      instructions.add(new PUSH(lr));

      // LDR r4, [sp, #4]
      instructions.add(new LDR(r4, new Operand2(sp, 4)));

      // LDR r5, #0
      instructions.add(new LDR( r5, 0));

      // CMP r4, r5
      instructions.add(new CMP(r4, new Operand2(r5)));

      // MOVGE r4, #1
      instructions.add(new MOV(r4, 1, Conditionals.GE));

      // MOVLT r4, #0
      instructions.add(new MOV(r4, 0, Conditionals.LT));

      // CMP r4, #0
      instructions.add(new CMP(r4, 0));

      // BEQ L0
      instructions.add(new Branch("absL0", Conditionals.EQ));

      // LDR r4, [sp, #4]
      instructions.add(new LDR(r4, new Operand2(sp, 4)));

      // MOV r0, r4
      instructions.add(new MOV(r0, new Operand2(r4)));

      // POP{PC}
      instructions.add(new POP(pc));

      // B L1
      instructions.add(new Branch("absL1"));

      // L0:
      instructions.add(new LABEL("absL0:"));

      // SUB sp, sp, #4
      instructions.add(new SUB(sp, sp, new Operand2(4)));

      //LDR r4, [sp, #8]
      instructions.add(new LDR(r4, new Operand2(sp,8)));

      //LDR r5, [sp, #8]
      instructions.add(new LDR(r5, new Operand2(sp,8)));

      //LDR r6, =2
      instructions.add(new LDR(r6, 2));

      //SMULL r5, r6, r5, r6
      instructions.add(new SMULL(r5,r6,r5,r6));

      //CMP r6, r5, ASR #31
      instructions.add(new CMP(r6,r5, new Operand2(31)));

      //BLNE p_throw_overflow_error
      instructions.add(new Branch(("p_throw_overflow_error"), Conditionals.NE).setSuffix("L"));

      //SUBS r4, r4, r5
      instructions.add(new SUB(r4, r4, new Operand2(r5), Flags.S));

      //BLVS p_throw_overflow_error
      instructions.add(new Branch("p_throw_overflow_error", Conditionals.VS).setSuffix("L"));

      //STR r4, [sp]
      instructions.add(new STR(r4, new Operand2(sp)));

      //LDR r4, [sp]
      instructions.add(new LDR(r4, new Operand2(sp)));

      //MOV r0, r4
      instructions.add(new MOV(r0, new Operand2(r4)));

      //ADD sp, sp, #4
      instructions.add(new ADD(sp, sp, new Operand2(4)));

      //POP {pc}
      instructions.add(new POP(pc));

      //ADD sp, sp, #4
      instructions.add(new ADD(sp, sp, new Operand2(4)));

      //L1:
      instructions.add(new LABEL("absL1:"));

      //POP {pc}
      instructions.add(new POP(pc));

      //.ltorg
      instructions.add(new Directive(LTORG));

      return instructions;
    }

    private static List<Instruction> isAlnumInstructions(){
      List<Instruction> instructions = new ArrayList<>();

      instructions.add(new LABEL("char_f_isAlnum:"));
      instructions.add(new PUSH(lr));
      instructions.add(new SUB(sp, sp ,new Operand2(4)));
      instructions.add(new LDR(r4, new Operand2(sp, 8), "SB"));
      instructions.add(new STR(r4, new Operand2(sp)));
      instructions.add(new LDR(r4, new Operand2(sp)));
      instructions.add(new LDR(r5, 65));
      instructions.add(new CMP(r4, new Operand2(r5)));
      instructions.add(new MOV(r4, 1, Conditionals.GE));
      instructions.add(new MOV(r4, 0, Conditionals.LT));
      instructions.add(new LDR(r5, new Operand2(sp)));
      instructions.add(new LDR(r6, 90));
      instructions.add(new CMP(r5, new Operand2(r6)));
      instructions.add(new MOV(r5, 1, Conditionals.LE));
      instructions.add(new MOV(r5, 0, Conditionals.GT));
      instructions.add(new BoolOp(BoolOpType.AND, r4, r4, r5));
      instructions.add(new LDR(r5, new Operand2(sp)));
      instructions.add(new LDR(r6, 97));
      instructions.add(new CMP(r5, new Operand2(r6)));
      instructions.add(new MOV(r5,1,Conditionals.GE));
      instructions.add(new MOV(r5, 0, Conditionals.LT));
      instructions.add(new LDR(r6, new Operand2(sp)));
      instructions.add(new LDR(r7, 122));
      instructions.add(new CMP(r6, new Operand2(r7)));
      instructions.add(new MOV(r6,1,Conditionals.LE));
      instructions.add(new MOV(r6,0,Conditionals.GT));
      instructions.add(new BoolOp(BoolOpType.AND, r5, r5, r6));
      instructions.add(new BoolOp(BoolOpType.ORR, r4, r4 ,r5));
      instructions.add(new LDR(r5, new Operand2(sp)));
      instructions.add(new LDR(r6, 48));
      instructions.add(new CMP(r5, new Operand2(r6)));
      instructions.add(new MOV(r5, 1, Conditionals.GE));
      instructions.add(new MOV(r5, 0, Conditionals.LT));
      instructions.add(new LDR(r6, new Operand2(sp)));
      instructions.add(new LDR(r7, 57));
      instructions.add(new CMP(r6, new Operand2(r7)));
      instructions.add(new MOV(r6, 1, Conditionals.LE));
      instructions.add(new MOV(r6, 0, Conditionals.GT));
      instructions.add(new BoolOp(BoolOpType.AND, r5, r5, r6));
      instructions.add(new BoolOp(BoolOpType.ORR, r4, r4 ,r5));
      instructions.add(new CMP(r4, 0));
      instructions.add(new Branch("isAlnumL0", Conditionals.EQ));
      instructions.add(new MOV(r4, 1));
      instructions.add(new MOV(r0, new Operand2(r4)));
      instructions.add(new ADD(sp, sp, new Operand2(4)));
      instructions.add(new POP(pc));
      instructions.add(new Branch("isAlnumL1"));
      instructions.add(new LABEL("isAlnumL0:"));
      instructions.add(new MOV(r4, 0));
      instructions.add(new MOV(r0, new Operand2(r4)));
      instructions.add(new ADD(sp, sp ,new Operand2(4)));
      instructions.add(new POP(pc));
      instructions.add(new LABEL("isAlnumL1:"));
      instructions.add(new POP(pc));
      instructions.add(new Directive(LTORG));

      return instructions;
    }

  private static List<Instruction> isAlphaInstructions(){
    List<Instruction> instructions = new ArrayList<>();

    instructions.add(new LABEL("char_f_isAlpha:"));
    instructions.add(new PUSH(lr));
    instructions.add(new SUB(sp, sp ,new Operand2(4)));
    instructions.add(new LDR(r4, new Operand2(sp, 8), "SB"));
    instructions.add(new STR(r4, new Operand2(sp)));
    instructions.add(new LDR(r4, new Operand2(sp)));
    instructions.add(new LDR(r5, 65));
    instructions.add(new CMP(r4, new Operand2(r5)));
    instructions.add(new MOV(r4, 1, Conditionals.GE));
    instructions.add(new MOV(r4, 0, Conditionals.LT));
    instructions.add(new LDR(r5, new Operand2(sp)));
    instructions.add(new LDR(r6, 90));
    instructions.add(new CMP(r5, new Operand2(r6)));
    instructions.add(new MOV(r5, 1, Conditionals.LE));
    instructions.add(new MOV(r5, 0, Conditionals.GT));
    instructions.add(new BoolOp(BoolOpType.AND, r4, r4, r5));
    instructions.add(new LDR(r5, new Operand2(sp)));
    instructions.add(new LDR(r6, 97));
    instructions.add(new CMP(r5, new Operand2(r6)));
    instructions.add(new MOV(r5,1,Conditionals.GE));
    instructions.add(new MOV(r5, 0, Conditionals.LT));
    instructions.add(new LDR(r6, new Operand2(sp)));
    instructions.add(new LDR(r7, 122));
    instructions.add(new CMP(r6, new Operand2(r7)));
    instructions.add(new MOV(r6,1,Conditionals.LE));
    instructions.add(new MOV(r6,0,Conditionals.GT));
    instructions.add(new BoolOp(BoolOpType.AND, r5, r5, r6));
    instructions.add(new BoolOp(BoolOpType.ORR, r4, r4 ,r5));
    instructions.add(new CMP(r4, 0));
    instructions.add(new Branch("isAlphaL0", Conditionals.EQ));
    instructions.add(new MOV(r4, 1));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp, new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new Branch("isAlphaL1"));
    instructions.add(new LABEL("isAlphaL0:"));
    instructions.add(new MOV(r4, 0));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp ,new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new LABEL("isAlphaL1:"));
    instructions.add(new POP(pc));
    instructions.add(new Directive(LTORG));


    return instructions;
  }

  private static List<Instruction> isAsciiInstructions() {
    List<Instruction> instructions = new ArrayList<>();
    instructions.add(new LABEL("char_f_isAscii:"));
    instructions.add(new PUSH(lr));
    instructions.add(new SUB(sp, sp ,new Operand2(4)));
    instructions.add(new LDR(r4, new Operand2(sp, 8), "SB"));
    instructions.add(new STR(r4, new Operand2(sp)));
    instructions.add(new LDR(r4, new Operand2(sp)));
    instructions.add(new LDR(r5, 0));
    instructions.add(new CMP(r4, new Operand2(r5)));
    instructions.add(new MOV(r4, 1, Conditionals.GE));
    instructions.add(new MOV(r4, 0, Conditionals.LT));
    instructions.add(new LDR(r5, new Operand2(sp)));
    instructions.add(new LDR(r6, 127));
    instructions.add(new CMP(r5, new Operand2(r6)));
    instructions.add(new MOV(r5, 1, Conditionals.LE));
    instructions.add(new MOV(r5, 0, Conditionals.GT));
    instructions.add(new BoolOp(BoolOpType.AND, r4, r4, r5));
    instructions.add(new CMP(r4, 0));
    instructions.add(new Branch("isAsciiL0", Conditionals.EQ));
    instructions.add(new MOV(r4, 1));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp, new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new Branch("isAsciiL1"));
    instructions.add(new LABEL("isAsciiL0:"));
    instructions.add(new MOV(r4, 0));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp ,new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new LABEL("isAsciiL1:"));
    instructions.add(new POP(pc));
    instructions.add(new Directive(LTORG));

    return instructions;
    }

  private static List<Instruction> isDigitInstructions() {
    List<Instruction> instructions = new ArrayList<>();
    instructions.add(new LABEL("char_f_isDigit:"));
    instructions.add(new PUSH(lr));
    instructions.add(new SUB(sp, sp ,new Operand2(4)));
    instructions.add(new LDR(r4, new Operand2(sp, 8), "SB"));
    instructions.add(new STR(r4, new Operand2(sp)));
    instructions.add(new LDR(r4, new Operand2(sp)));
    instructions.add(new LDR(r5, 48));
    instructions.add(new CMP(r4, new Operand2(r5)));
    instructions.add(new MOV(r4, 1, Conditionals.GE));
    instructions.add(new MOV(r4, 0, Conditionals.LT));
    instructions.add(new LDR(r5, new Operand2(sp)));
    instructions.add(new LDR(r6, 57));
    instructions.add(new CMP(r5, new Operand2(r6)));
    instructions.add(new MOV(r5, 1, Conditionals.LE));
    instructions.add(new MOV(r5, 0, Conditionals.GT));
    instructions.add(new BoolOp(BoolOpType.AND, r4, r4, r5));
    instructions.add(new CMP(r4, 0));
    instructions.add(new Branch("isDigitL0", Conditionals.EQ));
    instructions.add(new MOV(r4, 1));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp, new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new Branch("isDigitL1"));
    instructions.add(new LABEL("isDigitL0:"));
    instructions.add(new MOV(r4, 0));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp ,new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new LABEL("isDigitL1:"));
    instructions.add(new POP(pc));
    instructions.add(new Directive(LTORG));


    return instructions;
    }

  private static List<Instruction> isLowerInstructions() {
    List<Instruction> instructions = new ArrayList<>();
    instructions.add(new LABEL("char_f_isLower:"));
    instructions.add(new PUSH(lr));
    instructions.add(new SUB(sp, sp ,new Operand2(4)));
    instructions.add(new LDR(r4, new Operand2(sp, 8), "SB"));
    instructions.add(new STR(r4, new Operand2(sp)));
    instructions.add(new LDR(r4, new Operand2(sp)));
    instructions.add(new LDR(r5, 97));
    instructions.add(new CMP(r4, new Operand2(r5)));
    instructions.add(new MOV(r4, 1, Conditionals.GE));
    instructions.add(new MOV(r4, 0, Conditionals.LT));
    instructions.add(new LDR(r5, new Operand2(sp)));
    instructions.add(new LDR(r6, 122));
    instructions.add(new CMP(r5, new Operand2(r6)));
    instructions.add(new MOV(r5, 1, Conditionals.LE));
    instructions.add(new MOV(r5, 0, Conditionals.GT));
    instructions.add(new BoolOp(BoolOpType.AND, r4, r4, r5));
    instructions.add(new CMP(r4, 0));
    instructions.add(new Branch("isLowerL0", Conditionals.EQ));
    instructions.add(new MOV(r4, 1));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp, new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new Branch("isLowerL1"));
    instructions.add(new LABEL("isLowerL0:"));
    instructions.add(new MOV(r4, 0));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp ,new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new LABEL("isLowerL1:"));
    instructions.add(new POP(pc));
    instructions.add(new Directive(LTORG));


    return instructions;
  }

  private static List<Instruction> isUpperInstructions() {
    List<Instruction> instructions = new ArrayList<>();
    instructions.add(new LABEL("char_f_isUpper:"));
    instructions.add(new PUSH(lr));
    instructions.add(new SUB(sp, sp ,new Operand2(4)));
    instructions.add(new LDR(r4, new Operand2(sp, 8), "SB"));
    instructions.add(new STR(r4, new Operand2(sp)));
    instructions.add(new LDR(r4, new Operand2(sp)));
    instructions.add(new LDR(r5, 65));
    instructions.add(new CMP(r4, new Operand2(r5)));
    instructions.add(new MOV(r4, 1, Conditionals.GE));
    instructions.add(new MOV(r4, 0, Conditionals.LT));
    instructions.add(new LDR(r5, new Operand2(sp)));
    instructions.add(new LDR(r6, 90));
    instructions.add(new CMP(r5, new Operand2(r6)));
    instructions.add(new MOV(r5, 1, Conditionals.LE));
    instructions.add(new MOV(r5, 0, Conditionals.GT));
    instructions.add(new BoolOp(BoolOpType.AND, r4, r4, r5));
    instructions.add(new CMP(r4, 0));
    instructions.add(new Branch("isUpperL0", Conditionals.EQ));
    instructions.add(new MOV(r4, 1));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp, new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new Branch("isUpperL1"));
    instructions.add(new LABEL("isUpperL0:"));
    instructions.add(new MOV(r4, 0));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp ,new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new LABEL("isUpperL1:"));
    instructions.add(new POP(pc));
    instructions.add(new Directive(LTORG));


    return instructions;
  }

  private static List<Instruction> isSpaceInstructions() {
    List<Instruction> instructions = new ArrayList<>();
    instructions.add(new LABEL("char_f_isSpace:"));
    instructions.add(new PUSH(lr));
    instructions.add(new SUB(sp, sp ,new Operand2(4)));
    instructions.add(new LDR(r4, new Operand2(sp, 8), "SB"));
    instructions.add(new STR(r4, new Operand2(sp)));
    instructions.add(new LDR(r4, new Operand2(sp)));
    instructions.add(new LDR(r5, 32));
    instructions.add(new CMP(r4, new Operand2(r5)));
    instructions.add(new MOV(r4, 1, Conditionals.EQ));
    instructions.add(new MOV(r4, 0, Conditionals.NE));
    instructions.add(new CMP(r4, 0));
    instructions.add(new Branch("isSpaceL0", Conditionals.EQ));
    instructions.add(new MOV(r4, 1));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp, new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new Branch("isSpaceL1"));
    instructions.add(new LABEL("isSpaceL0:"));
    instructions.add(new MOV(r4, 0));
    instructions.add(new MOV(r0, new Operand2(r4)));
    instructions.add(new ADD(sp, sp ,new Operand2(4)));
    instructions.add(new POP(pc));
    instructions.add(new LABEL("isSpaceL1:"));
    instructions.add(new POP(pc));
    instructions.add(new Directive(LTORG));

    return instructions;
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
        instructions.add(new Branch("maxL0_" + MAX.getIdent()));

        instructions.add(new LABEL("maxL1_" + MAX.getIdent() + ":"));
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
        instructions.add(new Branch("maxL2_" + MAX.getIdent(), Conditionals.EQ));
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
        instructions.add(new Branch("maxL3_" + MAX.getIdent()));

        instructions.add(new LABEL("maxL2_" + MAX.getIdent() + ":"));

        instructions.add(new LABEL("maxL3_" + MAX.getIdent() + ":"));
        instructions.add(new LDR(r4, new Operand2(sp, 4)));
        instructions.add(new LDR(r5, 1));
        instructions.add(new ADD(r4, r4, new Operand2(r5), Flags.S));
        instructions.add(new Branch("p_throw_overflow_error", Conditionals.VS).setSuffix("L"));
        instructions.add(new STR(r4, new Operand2(sp, 4)));

        instructions.add(new LABEL("maxL0_" + MAX.getIdent() + ":"));
        instructions.add(new LDR(r4, new Operand2(sp, 4)));
        instructions.add(new LDR(r5, new Operand2(sp, 12)));
        instructions.add(new LDR(r5, new Operand2(r5)));
        instructions.add(new CMP(r4, new Operand2(r5)));
        instructions.add(new MOV(r4, 1, Conditionals.LT));
        instructions.add(new MOV(r4, 0, Conditionals.GE));
        instructions.add(new CMP(r4, 1));
        instructions.add(new Branch("maxL1_" + MAX.getIdent(), Conditionals.EQ));
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
        instructions.add(new Branch("minL0_" + MIN.getIdent()));

        instructions.add(new LABEL("minL1_" + MIN.getIdent() + ":"));
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
        instructions.add(new Branch("minL2_" + MIN.getIdent(), Conditionals.EQ));
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
        instructions.add(new Branch("minL3_" + MIN.getIdent()));

        instructions.add(new LABEL("minL2_" + MIN.getIdent() + ":"));

        instructions.add(new LABEL("minL3_" + MIN.getIdent() + ":"));
        instructions.add(new LDR(r4, new Operand2(sp, 4)));
        instructions.add(new LDR(r5, 1));
        instructions.add(new ADD(r4, r4, new Operand2(r5), Flags.S));
        instructions.add(new Branch("p_throw_overflow_error", Conditionals.VS).setSuffix("L"));
        instructions.add(new STR(r4, new Operand2(sp, 4)));

        instructions.add(new LABEL("minL0_" + MIN.getIdent() + ":"));
        instructions.add(new LDR(r4, new Operand2(sp, 4)));
        instructions.add(new LDR(r5, new Operand2(sp, 12)));
        instructions.add(new LDR(r5, new Operand2(r5)));
        instructions.add(new CMP(r4, new Operand2(r5)));
        instructions.add(new MOV(r4, 1, Conditionals.LT));
        instructions.add(new MOV(r4, 0, Conditionals.GE));
        instructions.add(new CMP(r4, 1));
        instructions.add(new Branch("minL1_" + MIN.getIdent(), Conditionals.EQ));
        instructions.add(new LDR(r4, new Operand2(sp)));
        instructions.add(new MOV(r0, new Operand2(r4)));
        instructions.add(new ADD(sp, sp, new Operand2(8)));

        instructions.add(new POP(pc));
        instructions.add(new POP(pc));
        instructions.add(new Directive(LTORG));

        return instructions;
    }

}
