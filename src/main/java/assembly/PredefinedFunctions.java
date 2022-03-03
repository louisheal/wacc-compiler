package assembly;

import java.util.ArrayList;
import java.util.List;

import static assembly.Instruction.InstrType.*;

public class PredefinedFunctions {

  // TODO: check cases for LDR r0, =msg_(number) for where the number is changed

  private static final List<Instruction> messages = new ArrayList<>();
  private static int msgCounter = 0;
  private static final Register r0 = new Register(0);
  private static final Register r1 = new Register(1);
  private static final Register r2 = new Register(2);
  private static final Register sp = new Register(13);

  public enum Functions {
    P_PRINT_INT, P_PRINT_BOOL, P_PRINT_STRING, P_PRINT_REFERENCE, P_PRINT_LN,
    P_CHECK_NULL_POINTER,
    P_THROW_RUNTIME_ERROR, P_THROW_OVERFLOW_ERROR,
    P_CHECK_DIVIDE_BY_ZERO,
    P_CHECK_ARRAY_BOUNDS,
    P_READ_INT, P_READ_CHAR,
    P_FREE_PAIR
  }

  public static List<Instruction> getFunctionInstructions(Functions function) {
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

  public void addMessage(Instruction instruction) {
    messages.add(instruction);
  }

  public void incrementMsgCounter() {
    msgCounter++;
  }

  private static List<Instruction> pPrintIntInstruction() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_int:
    instructions.add(new Instruction(LABEL, "p_print_int:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // MOV r1, r0
    instructions.add(new Instruction(MOV, r0, new Operand2(r1)));

    /* msg_(number):
              .word 3
		      .ascii	"%d\0" */
    messages.add(new Instruction(LABEL, "msg_" + msgCounter + ":"));
    messages.add(new Instruction(WORD, 3));
    messages.add(new Instruction(ASCII, "\"%d\\0\""));

    // LDR r0, =msg_0
    instructions.add(new Instruction(LDR, r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new Instruction(ADD, r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new Instruction(BL, "printf"));

    // MOV r0, #0
    instructions.add(new Instruction(MOV, r0, new Operand2(0)));

    // BL fflush
    instructions.add(new Instruction(BL, "fflush"));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pPrintBoolInstruction() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_bool:
    instructions.add(new Instruction(LABEL, "p_print_bool:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // CMP r0, #0
    instructions.add(new Instruction(CMP, r0, new Operand2(0)));


     /* msg_(number):
     	      .word 5
     	      .ascii	"true\0" */
    messages.add(new Instruction(LABEL, "msg_" + msgCounter + ":"));
    messages.add(new Instruction(WORD, 5));
    messages.add(new Instruction(ASCII, "\"true\\0\""));

    // LDRNE r0, =msg_n
    instructions.add(new Instruction(LDR, r0, "msg_" + msgCounter, Conditionals.NE));
    msgCounter++;

    /* msg_(number + 1):
     	     .word 6
     	     .ascii	"false\0" */

    messages.add(new Instruction(LABEL, "msg_" + msgCounter + ":"));
    messages.add(new Instruction(WORD, 6));
    messages.add(new Instruction(ASCII, "\"false\\0\""));

    // LDREQ r0, =msg_n
    instructions.add(new Instruction(LDR, r0, "msg_" + msgCounter, Conditionals.EQ));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new Instruction(ADD, r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new Instruction(BL, "printf"));

    // MOV r0, #0
    instructions.add(new Instruction(MOV, r0, new Operand2(0)));

    // BL fflush
    instructions.add(new Instruction(BL, "fflush"));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pPrintStringInstruction() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_string:
    instructions.add(new Instruction(LABEL, "p_print_string:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // LDR r1, [r0]
    instructions.add(new Instruction(LDR, r1, new Operand2(r0)));

    // ADD r2, r0, #4
    instructions.add(new Instruction(ADD, r2, r0, new Operand2(4)));

     /* msg_n:
     	      .word 5
     	      .ascii	"%.*s\0" */

    //TODO: Ensure that the actual string is stored in a message in visit

    messages.add(new Instruction(LABEL, "msg_" + msgCounter + ":"));
    messages.add(new Instruction(WORD, 5));
    messages.add(new Instruction(ASCII, "\"%.*s\\0\""));

    // LDR r0, =msg_n
    instructions.add(new Instruction(LDR, r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new Instruction(ADD, r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new Instruction(BL, "printf"));

    // MOV r0, #0
    instructions.add(new Instruction(MOV, r0, new Operand2(0)));

    //	BL fflush
    instructions.add(new Instruction(BL, "fflush"));

    //	POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pPrintReferenceInstruction() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_reference:
    instructions.add(new Instruction(LABEL, "p_print_reference:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // MOV r1, r0
    instructions.add(new Instruction(MOV, r1, new Operand2(r0)));

   	/* msg_n:
   		    .word 3
   		    .ascii	"%p\0" */

    messages.add(new Instruction(LABEL, "msg_" + msgCounter + ":"));
    messages.add(new Instruction(WORD, 3));
    messages.add(new Instruction(ASCII, "\"%p\\0\""));

    // LDR r0, =msg_n
    instructions.add(new Instruction(LDR, r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new Instruction(ADD, r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new Instruction(BL, "printf"));

    // MOV r0, #0
    instructions.add(new Instruction(MOV, r0, new Operand2(0)));

    // BL fflush
    instructions.add(new Instruction(BL, "fflush"));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pPrintLn() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_ln:
    instructions.add(new Instruction(LABEL, "p_print_ln:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    /* msg_n:
            .word 1
            .ascii	"\0" */

    messages.add(new Instruction(LABEL, "msg_" + msgCounter + ":"));
    messages.add(new Instruction(WORD, 1));
    messages.add(new Instruction(ASCII, "\"\\0\""));

    // LDR r0, =msg_n
    instructions.add(new Instruction(LDR, r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new Instruction(ADD, r0, r0, new Operand2(4)));

    // BL puts
    instructions.add(new Instruction(BL, "puts"));

    // MOV r0, #0
    instructions.add(new Instruction(MOV, r0, new Operand2(0)));

    // BL fflush
    instructions.add(new Instruction(BL, "fflush"));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pCheckNullPointer() {

    List<Instruction> instructions = new ArrayList<>();

    // p_check_null_pointer:
    instructions.add(new Instruction(LABEL, "p_check_null_pointer:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // CMP r0, #0
    instructions.add(new Instruction(CMP, r0, new Operand2(0)));

    /* msg_n:
    	      .word 50
    	      .ascii	"NullReferenceError: dereference a null reference\n\0" */

    messages.add(new Instruction(LABEL, "msg_" + msgCounter + ":"));
    messages.add(new Instruction(WORD, 50));
    messages.add(new Instruction(ASCII, "\"NullReferenceError: dereference a null reference\\n\\0\""));

    // LDREQ r0, =msg_n
    instructions.add(new Instruction(LDR, r0, "msg_" + msgCounter, Conditionals.EQ));
    msgCounter++;

    // BLEQ p_throw_runtime_error
    instructions.add(new Instruction(BL, "p_throw_runtime_error", Conditionals.EQ));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pThrowRuntimeError() {

    List<Instruction> instructions = new ArrayList<>();

    // p_throw_runtime_error:
    instructions.add(new Instruction(LABEL, "p_throw_runtime_error:"));

    // BL p_print_string
    instructions.add(new Instruction(BL, "p_print_string"));

    // MOV r0, #-1
    instructions.add(new Instruction(MOV, r0, new Operand2(-1)));

    // BL exit
    instructions.add(new Instruction(BL, "exit"));

    return instructions;
  }

  private static List<Instruction> pThrowOverflowError() {

    List<Instruction> instructions = new ArrayList<>();

    // p_throw_overflow_error:
    instructions.add(new Instruction(LABEL, "p_throw_overflow_error:"));

    /* message = .word 83
                 .ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n\0" */

    messages.add(new Instruction(LABEL, "msg_" + msgCounter + ":"));
    messages.add(new Instruction(WORD, 83));
    messages.add(new Instruction(ASCII, "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0\""));

    // LDR r0, =msg_n
    instructions.add(new Instruction(LDR, r0, "msg_" + msgCounter));
    msgCounter++;

    // BL p_throw_runtime_error
    instructions.add(new Instruction(BL, "p_throw_runtime_error"));

    return instructions;
  }

  private static List<Instruction> pCheckDivideByZero() {

    List<Instruction> instructions = new ArrayList<>();

    // p_check_divide_by_zero:
    instructions.add(new Instruction(LABEL, "p_check_divide_by_zero:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // CMP r1, #0
    instructions.add(new Instruction(CMP, r1, new Operand2(0)));

    /* message = .word 45
                 .ascii	"DivideByZeroError: divide or modulo by zero\n\0" */
    // LDREQ r0, =msg_0
    instructions.add(new Instruction(LDR, r0, "msg_0", Conditionals.EQ));

    // BLEQ p_throw_runtime_error
    instructions.add(new Instruction(BL, "p_throw_runtime_error", Conditionals.EQ));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pCheckArrayBounds() {

    List<Instruction> instructions = new ArrayList<>();

    // p_check_array_bounds:
    instructions.add(new Instruction(LABEL, "p_check_array_bounds:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // CMP r0, #0
    instructions.add(new Instruction(CMP, r0, new Operand2(0)));

    /* message = .word 44
           		 .ascii	"ArrayIndexOutOfBoundsError: negative index */
    // LDRLT r0, =msg_0
    instructions.add(new Instruction(LDR, r0, "msg_0", Conditionals.LT));

    // BLLT p_throw_runtime_error
    instructions.add(new Instruction(BL, "p_throw_runtime_error", Conditionals.LT));

    // LDR r1, [r1]
    instructions.add(new Instruction(LDR, r1, new Operand2(r1)));

    // CMP r0, r1
    instructions.add(new Instruction(CMP, r0, new Operand2(r1)));

    // LDRCS r0, =msg_0
    instructions.add(new Instruction(LDR, "msg_0", Conditionals.CS));

    // BLCS p_throw_runtime_error
    instructions.add(new Instruction(BL, "p_throw_runtime_error", Conditionals.CS));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pReadInt() {

    List<Instruction> instructions = new ArrayList<>();

    // p_read_int:
    instructions.add(new Instruction(LABEL, "p_read_int:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // MOV r1, r0
    instructions.add(new Instruction(MOV, r1, new Operand2(r0)));

    /* message = .word 3
      		     .ascii	"%d\0" */
    // LDR r0, =msg_0
    instructions.add(new Instruction(LDR, r0, "msg_0"));

    // ADD r0, r0, #4
    instructions.add(new Instruction(ADD, r0, r0, new Operand2(4)));

    // BL scanf
    instructions.add(new Instruction(BL, "scanf"));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pReadChar() {

    List<Instruction> instructions = new ArrayList<>();

    // p_read_char:
    instructions.add(new Instruction(LABEL, "p_read_char:"));

    //  PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    //  MOV r1, r0
    instructions.add(new Instruction(MOV, r1, new Operand2(r0)));

    /* message = .word 4
      		     .ascii	" %c\0" */
    //  LDR r0, =msg_0
    instructions.add(new Instruction(LDR, r0, "msg_0"));

    //  ADD r0, r0, #4
    instructions.add(new Instruction(ADD, r0, r0, new Operand2(4)));

    //  	BL scanf
    instructions.add(new Instruction(BL, "scanf"));

    //	POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pFreePair() {

    List<Instruction> instructions = new ArrayList<>();

    // p_free_pair:
    instructions.add(new Instruction(LABEL, "p_free_pair:"));

    // PUSH {lr}
    instructions.add(new Instruction(LABEL, "PUSH {lr}"));

    // CMP r0, #0
    instructions.add(new Instruction(CMP, r0, new Operand2(0)));

    /* message = .word 50
		         .ascii	"NullReferenceError: dereference a null reference\n\0" */
    // LDREQ r0, =msg_0
    instructions.add(new Instruction(LDR, r0, "msg_0", Conditionals.EQ));

    // BEQ p_throw_runtime_error
    instructions.add(new Instruction(LABEL, "BEQ p_throw_runtime_error"));

    // PUSH {r0}
    instructions.add(new Instruction(PUSH, r0));

    // LDR r0, [r0]
    instructions.add(new Instruction(LDR, r0, new Operand2(r0)));

    // BL free
    instructions.add(new Instruction(BL, "free"));

    // LDR r0, [sp]
    instructions.add(new Instruction(LDR, r0, new Operand2(sp)));

    // LDR r0, [r0, #4]
    instructions.add(new Instruction(LDR, r0, new Operand2(r0, 4)));

    // BL free
    instructions.add(new Instruction(BL, "free"));

    // POP {r0}
    instructions.add(new Instruction(POP, r0));

    // BL free
    instructions.add(new Instruction(BL, "free"));

    // POP {pc}
    instructions.add(new Instruction(LABEL, "POP {pc}"));

    return instructions;
  }

}
