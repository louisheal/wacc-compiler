package assembly;

import com.sun.jdi.PrimitiveValue;
import java.util.ArrayList;
import java.util.List;

public class PredefinedFunctions {

  private final Register r0 = new Register(0);
  private final Register r1 = new Register(1);
  private final Register r2 = new Register(2);

  public enum Functions {
    P_PRINT_INT, P_PRINT_BOOL, P_PRINT_STRING, P_PRINT_REFERENCE, P_PRINT_LN,
    P_CHECK_NULL_POINTER,
    P_THROW_RUNTIME_ERROR, P_THROW_OVERFLOW_ERROR, P_THROW_OVERFLOW_ERROR_NE,
    P_CHECK_DIVIDE_BY_ZERO,
    P_CHECK_ARRAY_BOUNDS,
    P_READ_INT, P_READ_CHAR,
    P_FREE_PAIR
  }

  public List<Instruction> getFunctionInstructions(Functions function) {
    List<Instruction> instructions = new ArrayList<>();

    switch (function) {
      case P_PRINT_INT:
        instructions.addAll(pPrintIntInstruction());
        break;
      case P_PRINT_BOOL:
        instructions.addAll(pPrintBoolInstruction());
        break;
      case P_PRINT_STRING:
        instructions.addAll(pPrintStringInstruction());
        break;
      case P_PRINT_REFERENCE:
        instructions.addAll(pPrintReferenceInstruction());
        break;
      case P_PRINT_LN:
        instructions.addAll(pPrintLn());
        break;
      case P_CHECK_NULL_POINTER:
        instructions.addAll(pCheckNullPointer());
        break;
      case P_THROW_RUNTIME_ERROR:
        instructions.addAll(pThrowRuntimeError());
        break;
      case P_THROW_OVERFLOW_ERROR:
        instructions.addAll(pThrowOverflowError());
        break;
      case P_THROW_OVERFLOW_ERROR_NE:
        instructions.addAll(pThrowOverflowErrorNE());
        break;
      case P_CHECK_DIVIDE_BY_ZERO:
        instructions.addAll(pCheckDivideByZero());
        break;
      case P_CHECK_ARRAY_BOUNDS:
        instructions.addAll(pCheckArrayBounds());
        break;
      case P_READ_INT:
        instructions.addAll(pReadInt());
        break;
      case P_READ_CHAR:
        instructions.addAll(pReadChar());
        break;
      case P_FREE_PAIR:
        instructions.addAll(pFreePair());
        break;
    }

    return instructions;
  }

  private List<Instruction> pPrintIntInstruction() {
    List<Instruction> instructions = new ArrayList<>();

    // PUSH {lr}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "PUSH {lr}"));

    // MOV r1, r0
    instructions.add(new Instruction(Instruction.InstrType.MOV, r0, new Operand2(r1)));

    // TODO: sort out how messages will be added
    // LDR r0, =msg_0
    instructions.add(new Instruction(Instruction.InstrType.LDR, r0, "msg_0"));

    // ADD r0, r0, #4
    instructions.add(new Instruction(Instruction.InstrType.ADD, r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new Instruction(Instruction.InstrType.BL, "printf"));

    //    	MOV r0, #0
    instructions.add(new Instruction(Instruction.InstrType.MOV, r0, new Operand2(0)));

    // BL fflush
    instructions.add(new Instruction(Instruction.InstrType.BL, "fflush"));

    // POP {pc}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "POP {pc}"));

    return instructions;
  }

  private List<Instruction> pPrintBoolInstruction() {
    List<Instruction> instructions = new ArrayList<>();
    //	PUSH {lr}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "PUSH {lr}"));

    //	CMP r0, #0
    instructions.add(new Instruction(Instruction.InstrType.CMP, r0, new Operand2(0)));

    //TODO: Sort out messages to be added

    //	LDRNE r0, =msg_0
    instructions.add(new Instruction(Instruction.InstrType.LDR, r0, "msg_0", Conditionals.NE));

    //	LDREQ r0, =msg_1
    instructions.add(new Instruction(Instruction.InstrType.LDR, r0, "msg_1", Conditionals.EQ));

    //	ADD r0, r0, #4
    instructions.add(new Instruction(Instruction.InstrType.ADD, r0, r0, new Operand2(4)));

    //	BL printf
    instructions.add(new Instruction(Instruction.InstrType.BL, "printf"));

    //	MOV r0, #0
    instructions.add(new Instruction(Instruction.InstrType.MOV, r0, new Operand2(0)));

    //	BL fflush
    instructions.add(new Instruction(Instruction.InstrType.BL, "fflush"));

    //	POP {pc}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "POP {pc}"));

    return instructions;
  }

  private List<Instruction> pPrintStringInstruction() {
    List<Instruction> instructions = new ArrayList<>();

    // PUSH {lr}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "PUSH {lr}"));

    // LDR r1, [r0]
    instructions.add(new Instruction(Instruction.InstrType.LDR, r1, new Operand2(r0)));

    // ADD r2, r0, #4
    instructions.add(new Instruction(Instruction.InstrType.ADD, r2, r0, new Operand2(4)));

    // LDR r0, =msg_1
    instructions.add(new Instruction(Instruction.InstrType.LDR, r0, "msg_1"));

    // ADD r0, r0, #4
    instructions.add(new Instruction(Instruction.InstrType.ADD, r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new Instruction(Instruction.InstrType.BL, "printf"));

    // MOV r0, #0
    instructions.add(new Instruction(Instruction.InstrType.MOV, r0, new Operand2(0)));

    //	BL fflush
    instructions.add(new Instruction(Instruction.InstrType.BL, "fflush"));

    //	POP {pc}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "POP {pc}"));

    return instructions;
  }

  private List<Instruction> pPrintReferenceInstruction() {
    List<Instruction> instructions = new ArrayList<>();

    //	PUSH {lr}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "PUSH {lr}"));

    //	MOV r1, r0
    instructions.add(new Instruction(Instruction.InstrType.MOV, r1, new Operand2(r0)));

    //	LDR r0, =msg_0
    instructions.add(new Instruction(Instruction.InstrType.LDR, r0, "msg_0"));

    //	ADD r0, r0, #4
    instructions.add(new Instruction(Instruction.InstrType.ADD, r0, r0, new Operand2(4)));

    //	BL printf
    instructions.add(new Instruction(Instruction.InstrType.BL, "printf"));

    //	MOV r0, #0
    instructions.add(new Instruction(Instruction.InstrType.MOV, r0, new Operand2(0)));

    //	BL fflush
    instructions.add(new Instruction(Instruction.InstrType.BL, "fflush"));

    //	POP {pc}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "POP {pc}"));

    return instructions;
  }

  private List<Instruction> pPrintLn() {
    List<Instruction> instructions = new ArrayList<>();

    //	PUSH {lr}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "PUSH {lr}"));

    //	LDR r0, =msg_2
    instructions.add(new Instruction(Instruction.InstrType.LDR, r0, "msg_2"));

    //	ADD r0, r0, #4
    instructions.add(new Instruction(Instruction.InstrType.ADD, r0, r0, new Operand2(4)));

    //	BL puts
    instructions.add(new Instruction(Instruction.InstrType.BL, "puts"));

    //	MOV r0, #0
    instructions.add(new Instruction(Instruction.InstrType.MOV, r0, new Operand2(0)));

    //	BL fflush
    instructions.add(new Instruction(Instruction.InstrType.BL, "fflush"));

    //	POP {pc}
    instructions.add(new Instruction(Instruction.InstrType.LABEL, "POP {pc}"));

    return instructions;
  }

  private List<Instruction> pCheckNullPointer() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

  private List<Instruction> pThrowRuntimeError() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

  private List<Instruction> pThrowOverflowError() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

  private List<Instruction> pThrowOverflowErrorNE() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

  private List<Instruction> pCheckDivideByZero() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

  private List<Instruction> pCheckArrayBounds() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

  private List<Instruction> pReadInt() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

  private List<Instruction> pReadChar() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

  private List<Instruction> pFreePair() {
    List<Instruction> instructions = new ArrayList<>();

    return instructions;
  }

}
