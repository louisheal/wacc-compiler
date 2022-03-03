package assembly;

import java.util.ArrayList;
import java.util.List;

public class PredefinedFunctions {

  public enum Functions {
    P_PRINT_INT, P_PRINT_BOOL, P_PRINT_STRING, P_PUTCHAR, P_PRINT_REFERENCE
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
      case P_PUTCHAR:
        instructions.addAll(pPutCharInstruction());
        break;
      case P_PRINT_REFERENCE:
        instructions.addAll(pPrintReferenceInstruction());
        break;
    }

    return instructions;
  }

  public List<Instruction> pPrintIntInstruction() {
    List<Instruction> instructions = new ArrayList<>();


    return instructions;
  }

  public List<Instruction> pPrintBoolInstruction() {
    List<Instruction> instructions = new ArrayList<>();


    return instructions;
  }

  public List<Instruction> pPrintStringInstruction() {
    List<Instruction> instructions = new ArrayList<>();


    return instructions;
  }

  public List<Instruction> pPutCharInstruction() {
    List<Instruction> instructions = new ArrayList<>();


    return instructions;
  }

  public List<Instruction> pPrintReferenceInstruction() {
    List<Instruction> instructions = new ArrayList<>();

    instructions.add(new Instruction(Instruction.InstrType.LABEL, "PUSH {lr}"));


    return instructions;
  }






}
