package assembly;

import assembly.instructions.Instruction;
import assembly.instructions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PredefinedFunctions {

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

  public static List<Instruction> getInstructions(Set<Functions> functions) {

    /* Add dependencies to the set. */

    if (functions.contains(Functions.P_CHECK_ARRAY_BOUNDS) ||
        functions.contains(Functions.P_CHECK_NULL_POINTER) ||
        functions.contains(Functions.P_CHECK_DIVIDE_BY_ZERO) ||
        functions.contains(Functions.P_THROW_OVERFLOW_ERROR) ||
        functions.contains(Functions.P_FREE_PAIR)) {
      functions.add(Functions.P_THROW_RUNTIME_ERROR);
    }

    if (functions.contains(Functions.P_THROW_RUNTIME_ERROR)) {
      functions.add(Functions.P_PRINT_STRING);
    }

    /* Load the list of instructions. */

    List<Instruction> instructions = new ArrayList<>();

    for (Functions function : functions) {
      instructions.addAll(getFunctionInstructions(function));
    }

    return instructions;
  }

  private static List<Instruction> getFunctionInstructions(Functions function) {
    switch (function) {
      case P_PRINT_INT:
        return pPrintIntInstruction();
      case P_PRINT_BOOL:
        return pPrintBoolInstruction();
      case P_PRINT_STRING:
        return pPrintStringInstruction();
      case P_PRINT_REFERENCE:
        return pPrintReferenceInstruction();
      case P_PRINT_LN:
        return pPrintLn();
      case P_CHECK_NULL_POINTER:
        return pCheckNullPointer();
      case P_THROW_RUNTIME_ERROR:
        return pThrowRuntimeError();
      case P_THROW_OVERFLOW_ERROR:
        return pThrowOverflowError();
      case P_CHECK_DIVIDE_BY_ZERO:
        return pCheckDivideByZero();
      case P_CHECK_ARRAY_BOUNDS:
        return pCheckArrayBounds();
      case P_READ_INT:
        return pReadInt();
      case P_READ_CHAR:
        return pReadChar();
      case P_FREE_PAIR:
        return pFreePair();
    }
    return List.of();
  }

  public static void addMessage(Instruction instruction) {
    messages.add(instruction);
  }

  public static String getMessageLabel() {
    int msgNum = msgCounter;
    msgCounter++;
    return "msg_" + msgNum;
  }

  public static List<Instruction> getMessages() {
    return messages;
  }

  private static List<Instruction> pPrintIntInstruction() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_int:
    instructions.add(new LABEL("p_print_int:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // MOV r1, r0
    instructions.add(new MOV(r1, new Operand2(r0)));

    /* msg_(number):
              .word 3
		      .ascii	"%d\0" */
    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(3));
    messages.add(new ASCII("\"%d\\0\""));

    // LDR r0, =msg_0
    instructions.add(new LDR(r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new ADD(r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new BL("printf"));

    // MOV r0, #0
    instructions.add(new MOV(r0, new Operand2(0)));

    // BL fflush
    instructions.add(new BL("fflush"));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pPrintBoolInstruction() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_bool:
    instructions.add(new LABEL("p_print_bool:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // CMP r0, #0
    instructions.add(new CMP(r0, new Operand2(0)));


     /* msg_(number):
     	      .word 5
     	      .ascii	"true\0" */
    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(5));
    messages.add(new ASCII( "\"true\\0\""));

    // LDRNE r0, =msg_n
    instructions.add(new LDR(r0, "msg_" + msgCounter, Conditionals.NE));
    msgCounter++;

    /* msg_(number + 1):
     	     .word 6
     	     .ascii	"false\0" */

    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(6));
    messages.add(new ASCII("\"false\\0\""));

    // LDREQ r0, =msg_n
    instructions.add(new LDR(r0, "msg_" + msgCounter, Conditionals.EQ));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new ADD(r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new BL("printf"));

    // MOV r0, #0
    instructions.add(new MOV(r0, new Operand2(0)));

    // BL fflush
    instructions.add(new BL("fflush"));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pPrintStringInstruction() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_string:
    instructions.add(new LABEL("p_print_string:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // LDR r1, [r0]
    instructions.add(new LDR(r1, new Operand2(r0)));

    // ADD r2, r0, #4
    instructions.add(new ADD(r2, r0, new Operand2(4)));

     /* msg_n:
     	      .word 5
     	      .ascii	"%.*s\0" */

    // The string is stored in visitStringLiter

    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(5));
    messages.add(new ASCII("\"%.*s\\0\""));

    // LDR r0, =msg_n
    instructions.add(new LDR(r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new ADD(r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new BL("printf"));

    // MOV r0, #0
    instructions.add(new MOV(r0, new Operand2(0)));

    //	BL fflush
    instructions.add(new BL("fflush"));

    //	POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pPrintReferenceInstruction() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_reference:
    instructions.add(new LABEL("p_print_reference:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // MOV r1, r0
    instructions.add(new MOV(r1, new Operand2(r0)));

   	/* msg_n:
   		    .word 3
   		    .ascii	"%p\0" */

    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(3));
    messages.add(new ASCII("\"%p\\0\""));

    // LDR r0, =msg_n
    instructions.add(new LDR(r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new ADD(r0, r0, new Operand2(4)));

    // BL printf
    instructions.add(new BL("printf"));

    // MOV r0, #0
    instructions.add(new MOV(r0, new Operand2(0)));

    // BL fflush
    instructions.add(new BL( "fflush"));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pPrintLn() {

    List<Instruction> instructions = new ArrayList<>();

    // p_print_ln:
    instructions.add(new LABEL("p_print_ln:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    /* msg_n:
            .word 1
            .ascii	"\0" */

    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(1));
    messages.add(new ASCII("\"\\0\""));

    // LDR r0, =msg_n
    instructions.add(new LDR(r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new ADD(r0, r0, new Operand2(4)));

    // BL puts
    instructions.add(new BL("puts"));

    // MOV r0, #0
    instructions.add(new MOV(r0, new Operand2(0)));

    // BL fflush
    instructions.add(new BL("fflush"));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pCheckNullPointer() {

    List<Instruction> instructions = new ArrayList<>();

    // p_check_null_pointer:
    instructions.add(new LABEL("p_check_null_pointer:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // CMP r0, #0
    instructions.add(new CMP(r0, new Operand2(0)));

    /* msg_n:
    	      .word 50
    	      .ascii	"NullReferenceError: dereference a null reference\n\0" */

    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(50));
    messages.add(new ASCII("\"NullReferenceError: dereference a null reference\\n\\0\""));

    // LDREQ r0, =msg_n
    instructions.add(new LDR(r0, "msg_" + msgCounter, Conditionals.EQ));
    msgCounter++;

    // BLEQ p_throw_runtime_error
    instructions.add(new BL("p_throw_runtime_error", Conditionals.EQ));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pThrowRuntimeError() {

    List<Instruction> instructions = new ArrayList<>();

    // p_throw_runtime_error:
    instructions.add(new LABEL("p_throw_runtime_error:"));

    // BL p_print_string
    instructions.add(new BL("p_print_string"));

    // MOV r0, #-1
    instructions.add(new MOV(r0, new Operand2(-1)));

    // BL exit
    instructions.add(new BL("exit"));

    return instructions;
  }

  private static List<Instruction> pThrowOverflowError() {

    List<Instruction> instructions = new ArrayList<>();

    // p_throw_overflow_error:
    instructions.add(new LABEL("p_throw_overflow_error:"));

    /* message = .word 83
                 .ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n\0" */

    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(83));
    messages.add(new ASCII("\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0\""));

    // LDR r0, =msg_n
    instructions.add(new LDR(r0, "msg_" + msgCounter));
    msgCounter++;

    // BL p_throw_runtime_error
    instructions.add(new BL("p_throw_runtime_error"));

    return instructions;
  }

  private static List<Instruction> pCheckDivideByZero() {

    List<Instruction> instructions = new ArrayList<>();

    // p_check_divide_by_zero:
    instructions.add(new LABEL("p_check_divide_by_zero:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // CMP r1, #0
    instructions.add(new CMP(r1, new Operand2(0)));

    /* message = .word 45
                 .ascii	"DivideByZeroError: divide or modulo by zero\n\0" */

    messages.add(new LABEL( "msg_" + msgCounter + ":"));
    messages.add(new WORD(45));
    messages.add(new ASCII("\"DivideByZeroError: divide or modulo by zero\\n\\0\""));

    // LDREQ r0, =msg_0
    instructions.add(new LDR(r0, "msg_" + msgCounter, Conditionals.EQ));
    msgCounter++;

    // BLEQ p_throw_runtime_error
    instructions.add(new BL("p_throw_runtime_error", Conditionals.EQ));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pCheckArrayBounds() {

    List<Instruction> instructions = new ArrayList<>();

    // p_check_array_bounds:
    instructions.add(new LABEL("p_check_array_bounds:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // CMP r0, #0
    instructions.add(new CMP(r0, new Operand2(0)));

    /* message = .word 44
           		 .ascii	"ArrayIndexOutOfBoundsError: negative index */

    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(44));
    messages.add(new ASCII("\"ArrayIndexOutOfBoundsError: negative index\\n\\0\""));

    // LDRLT r0, =msg_0
    instructions.add(new LDR(r0, "msg_" + msgCounter, Conditionals.LT));
    msgCounter++;

    // BLLT p_throw_runtime_error
    instructions.add(new BL("p_throw_runtime_error", Conditionals.LT));

    // LDR r1, [r1]
    instructions.add(new LDR(r1, new Operand2(r1)));

    // CMP r0, r1
    instructions.add(new CMP(r0, new Operand2(r1)));


    /* msg_n:
         .word 45
         .ascii	"ArrayIndexOutOfBoundsError: index too large\n\0" */
    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(45));
    messages.add(new ASCII("\"ArrayIndexOutOfBoundsError: index too large\\n\\0\""));

    // LDRCS r0, =msg_n
    instructions.add(new LDR(r0, "msg_" + msgCounter, Conditionals.CS));
    msgCounter++;

    // BLCS p_throw_runtime_error
    instructions.add(new BL("p_throw_runtime_error", Conditionals.CS));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pReadInt() {

    List<Instruction> instructions = new ArrayList<>();

    // p_read_int:
    instructions.add(new LABEL("p_read_int:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // MOV r1, r0
    instructions.add(new MOV(r1, new Operand2(r0)));

    /* message = .word 3
      		     .ascii	"%d\0" */
    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(3));
    messages.add(new ASCII("\"%d\\0\""));

    // LDR r0, =msg_0
    instructions.add(new LDR(r0, "msg_" + msgCounter));
    msgCounter++;

    // ADD r0, r0, #4
    instructions.add(new ADD(r0, r0, new Operand2(4)));

    // BL scanf
    instructions.add(new BL("scanf"));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pReadChar() {

    List<Instruction> instructions = new ArrayList<>();

    // p_read_char:
    instructions.add(new LABEL("p_read_char:"));

    //  PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    //  MOV r1, r0
    instructions.add(new MOV(r1, new Operand2(r0)));

    /* message = .word 4
      		     .ascii	" %c\0" */
    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(4));
    messages.add(new ASCII("\" %c\\0\""));

    //  LDR r0, =msg_0
    instructions.add(new LDR(r0, "msg_" + msgCounter));
    msgCounter++;

    //  ADD r0, r0, #4
    instructions.add(new ADD(r0, r0, new Operand2(4)));

    //  	BL scanf
    instructions.add(new BL("scanf"));

    //	POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

  private static List<Instruction> pFreePair() {

    List<Instruction> instructions = new ArrayList<>();

    // p_free_pair:
    instructions.add(new LABEL("p_free_pair:"));

    // PUSH {lr}
    instructions.add(new LABEL("PUSH {lr}"));

    // CMP r0, #0
    instructions.add(new CMP(r0, new Operand2(0)));

    /* message = .word 50
		         .ascii	"NullReferenceError: dereference a null reference\n\0" */
    messages.add(new LABEL("msg_" + msgCounter + ":"));
    messages.add(new WORD(50));
    messages.add(new ASCII("\"NullReferenceError: dereference a null reference\\n\\0\""));

    // LDREQ r0, =msg_0
    instructions.add(new LDR(r0, "msg_" + msgCounter, Conditionals.EQ));
    msgCounter++;

    // BEQ p_throw_runtim
    instructions.add(new LABEL("BEQ p_throw_runtime_error"));

    // PUSH {r0}
    instructions.add(new PUSH(r0));

    // LDR r0, [r0]
    instructions.add(new LDR(r0, new Operand2(r0)));

    // BL free
    instructions.add(new BL("free"));

    // LDR r0, [sp]
    instructions.add(new LDR(r0, new Operand2(sp)));

    // LDR r0, [r0, #4]
    instructions.add(new LDR(r0, new Operand2(r0, 4)));

    // BL free
    instructions.add(new BL("free"));

    // POP {r0}
    instructions.add(new POP(r0));

    // BL free
    instructions.add(new BL("free"));

    // POP {pc}
    instructions.add(new LABEL("POP {pc}"));

    return instructions;
  }

}
