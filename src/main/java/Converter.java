import assembly.Conditionals;
import assembly.Flags;
import assembly.Instruction;
import assembly.Instruction.InstrType;
import assembly.Operand2;
import assembly.Register;
import ast.*;

import java.util.*;

public class Converter extends ASTVisitor<List<Instruction>> {

  /* A list of general purpose registers: r4, r5, r6, r7, r8, r9, r10 and r11. */
  List<Register> unusedRegisters = initialiseGeneralRegisters();

  /* Function return registers. */
  private final Register r0 = new Register(0);
  private final Register r1 = new Register(1);

  /* Special registers. */
  private final Register sp = new Register(13);
  private final Register pc = new Register(15);

  private int spLocation = 0;
  SymbolTable currentST;
  private boolean isDiv = false;
  private boolean isCalc = false;
  private boolean isArrayLookup = false;
  private boolean runtimeErr = false;

  private List<Instruction> getInstructionFromExpression(Expression expr) {

    if (expr == null) {
      return null;
    }

    switch (expr.getExprType()) {

      case INTLITER:
        return visitIntLiterExp(expr);

      case BOOLLITER:
        return visitBoolLiterExp(expr);

      case CHARLITER:
        return visitCharLiterExp(expr);

      case STRINGLITER:
        return visitStringLiterExp(expr);

      case IDENT:
        switch (currentST.getType(expr.getIdent()).getType()){
          case INT:
            return visitIntLiterExp(expr);
          case BOOL:
            return visitBoolLiterExp(expr);
          case CHAR:
            return visitCharLiterExp(expr);
          case STRING:
            return visitStringLiterExp(expr);
          case PAIR:
            List<Instruction> pairInstructions = new ArrayList<>();
            pairInstructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0),
                calculateMallocSize(expr, currentST.getType(expr.getIdent()))));
            pairInstructions.add(new Instruction(InstrType.BL, "malloc"));
            pairInstructions.addAll(getInstructionFromExpression(expr.getExpression1()));
            pairInstructions.addAll(getInstructionFromExpression(expr.getExpression2()));
            return pairInstructions;
          case ARRAY:
            List<Instruction> arrayInstructions = new ArrayList<>();
            arrayInstructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0),
                calculateMallocSize(expr, currentST.getType(expr.getIdent()))));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression arrayExp: expr.getArrayElem().getExpression()){
              arrayInstructions.addAll(getInstructionFromExpression(arrayExp));
            }
            return arrayInstructions;
        }

      case ARRAYELEM:
        List<Instruction> arrayInstructions = new ArrayList<>();
        switch (currentST.getType(expr.getArrayElem().getIdent()).getArrayType().getType()) {
          case INT:
            arrayInstructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0),
                8L * expr.getArrayElem().getExpression().size()));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element: expr.getArrayElem().getExpression()){
              arrayInstructions.addAll(visitIntLiterExp(element));
            }
            return arrayInstructions;
          case BOOL:
            arrayInstructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0),
                expr.getArrayElem().getExpression().size()));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element: expr.getArrayElem().getExpression()){
              arrayInstructions.addAll(visitBoolLiterExp(element));
            }
            return arrayInstructions;
          case CHAR:
            arrayInstructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0),
                expr.getArrayElem().getExpression().size()));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element: expr.getArrayElem().getExpression()){
              arrayInstructions.addAll(visitCharLiterExp(element));
            }
            return arrayInstructions;
          case STRING:
            long charCount = 0;
            for (Expression element: expr.getArrayElem().getExpression()){
              charCount += element.getStringLiter().length();
            }
            arrayInstructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0), charCount));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element: expr.getArrayElem().getExpression()){
              arrayInstructions.addAll(visitStringLiterExp(element));
            }
            return arrayInstructions;
          case PAIR:
            // TODO Ask about how the size of malloc works for pair
            long pairMallocSize = 0;

            for (Expression element : expr.getArrayElem().getExpression()) {
              pairMallocSize += calculateMallocSize(element,
                  currentST.getType(expr.getArrayElem().getIdent()));
            }

            arrayInstructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0), pairMallocSize));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element : expr.getArrayElem().getExpression()) {
              arrayInstructions.addAll(getInstructionFromExpression(element.getExpression1()));
              arrayInstructions.addAll(getInstructionFromExpression(element.getExpression2()));
            }
            return arrayInstructions;
          case ARRAY:
            long arrayMallocSize = 4;

            for (Expression element : expr.getArrayElem().getExpression()) {
              arrayMallocSize += calculateMallocSize(element,
                  currentST.getType(expr.getArrayElem().getIdent()));
            }

            arrayInstructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0), arrayMallocSize));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element : expr.getArrayElem().getExpression()) {
              arrayInstructions.addAll(getInstructionFromExpression(element));
            }
            return arrayInstructions;
        }

    }
    return null;
  }

  private long calculateMallocSize(Expression exp, Type type){
    switch(type.getType()){

      case PAIR:
        return 8;

      case ARRAY:
        long size = 0;
        for (Expression arrayExpression : exp.getArrayElem().getExpression()){
          size += calculateMallocSize(arrayExpression, type.getArrayType());
        }
        return size;
    }
    return sizeOfTypeOnStack(type);

  }

  private List<Register> initialiseGeneralRegisters() {

    List<Register> registers = new ArrayList<>();

    registers.add(new Register(4));
    registers.add(new Register(5));
    registers.add(new Register(6));
    registers.add(new Register(7));
    registers.add(new Register(8));
    registers.add(new Register(9));
    registers.add(new Register(10));
    registers.add(new Register(11));

    return registers;
  }

  private Register popUnusedRegister() {
    Register register = unusedRegisters.get(0);
    unusedRegisters.remove(register);
    return register;
  }

  private void pushUnusedRegister(Register register) {
    unusedRegisters.add(0, register);
  }

  private int sizeOfTypeOnStack(Type type) {
    switch (type.getType()) {
      case INT:
      case PAIR:
      case ARRAY:
      case STRING:
        return 4;
      case CHAR:
      case BOOL:
        return 1;
    }
    return 0;
  }

  private int totalBytesInScope(Statement statement) {

    //TODO: MAKE A SWITCH STATEMENT?

    if (statement.getStatType() == Statement.StatType.DECLARATION) {
      return sizeOfTypeOnStack(statement.getLhsType());
    }

    if (statement.getStatType() == Statement.StatType.CONCAT) {
      return totalBytesInScope(statement.getStatement1()) + totalBytesInScope(statement.getStatement2());
    }

    if (statement.getStatType() == Statement.StatType.WHILE) {
      return totalBytesInScope(statement) + maxBeginStatement(statement);
    }

    if (statement.getStatType() == Statement.StatType.IF) {
      int stat1Size = totalBytesInScope(statement.getStatement1());
      int stat2Size = totalBytesInScope(statement.getStatement2());
      return Math.max(stat1Size, stat2Size);
    }

    return -1;

  }

  private int totalBytesInProgram(Program program) {
    return totalBytesInScope(program.getStatement()) + maxBeginStatement(program.getStatement());
  }

  private int totalBytesInFunction(Function function) {
    return totalBytesInScope(function.getStatement()) + maxBeginStatement(function.getStatement());
  }

  private int maxBeginStatement(Statement statement) {

    List<Statement> beginStatements = getBeginStatements(statement);

    int size = 0;
    for (Statement s : beginStatements) {
      int sSize = totalBytesInScope(s);
      if (sSize > size) {
        size = sSize;
      }
    }

    return size;
  }

  private List<Statement> getBeginStatements(Statement statement) {

    List<Statement> beginStatements = new ArrayList<>();

    if (statement.getStatType() == Statement.StatType.BEGIN) {
      beginStatements.add(statement);
    }

    while (statement.getStatType() == Statement.StatType.CONCAT) {
      if (statement.getStatement1().getStatType() == Statement.StatType.BEGIN) {
        beginStatements.add(statement.getStatement1());
      }
      statement = statement.getStatement2();
    }

    if (statement.getStatType() == Statement.StatType.BEGIN) {
      beginStatements.add(statement);
    }

    return beginStatements;
  }

  public List<Instruction> arrayIndexOutOfBoundsError(List<Instruction> instructions, int msgNumber) {
    int offset = msgNumber * 3;
    instructions.add(1 + offset, new Instruction(InstrType.LABEL, "msg_" + msgNumber));
    instructions.add(2 + offset, new Instruction(InstrType.WORD, 44));
    instructions.add(3 + offset, new Instruction(InstrType.ASCII, "ArrayIndexOutOfBoundsError: negative index\n\0"));
    msgNumber++;
    offset = msgNumber * 3;
    instructions.add(1 + offset, new Instruction(InstrType.LABEL, "msg_" + msgNumber));
    instructions.add(2 + offset, new Instruction(InstrType.WORD, 45));
    instructions.add(3 + offset, new Instruction(InstrType.ASCII, "ArrayIndexOutOfBoundsError: index too large\n\0"));
    //TODO: Register Allocation
    instructions.add(new Instruction(InstrType.LABEL, "p_check_array_bounds:"));
    //TODO: ADD LR
    instructions.add(new Instruction(InstrType.LABEL, "PUSH {lr}"));
    instructions.add(new Instruction(InstrType.CMP, r0, 0));
    instructions.add(new Instruction(InstrType.LDR, r0, "msg_" + (msgNumber - 1), Conditionals.LT));
    instructions.add(new Instruction(InstrType.BL, "p_throw_runtime_error", Conditionals.LT));
    instructions.add(new Instruction(InstrType.LDR, r1, new Operand2(r1)));
    instructions.add(new Instruction(InstrType.CMP, r0, new Operand2(r1)));
    instructions.add(new Instruction(InstrType.LDR, r0, "msg_" + msgNumber, Conditionals.CS));
    instructions.add(new Instruction(InstrType.BL, "p_throw_runtime_error", Conditionals.CS));
    instructions.add(new Instruction(InstrType.LABEL, "POP {pc}"));
    runtimeErr = true;
    return instructions;
  }

  public List<Instruction> throwOverflowError(List<Instruction> instructions, int msgNumber) {
    int offset = msgNumber * 3;
    instructions.add(1 + offset, new Instruction(InstrType.LABEL, "msg_" + msgNumber));
    instructions.add(2 + offset, new Instruction(InstrType.WORD, 83));
    instructions.add(3 + offset, new Instruction(InstrType.ASCII, "OverflowError: the result is too " +
            "small/large to store in a 4-byte signed-integer.\n\0"));
    //TODO: Register Allocation
    instructions.add(new Instruction(InstrType.LABEL, "p_throw_overflow_error:"));
    instructions.add(new Instruction(InstrType.LDR, r0, "msg_" + msgNumber));
    instructions.add(new Instruction(InstrType.BL, "p_throw_runtime_error"));
    runtimeErr = true;
    return instructions;
  }

  public List<Instruction> checkDivideByZero(List<Instruction> instructions, int msgNumber) {
    int offset = msgNumber * 3;
    instructions.add(1 + offset, new Instruction(InstrType.LABEL, "msg_" + msgNumber));
    instructions.add(2 + offset, new Instruction(InstrType.WORD, 45));
    instructions.add(3 + offset, new Instruction(InstrType.ASCII, "DivideByZeroError: divide or modulo by zero\n\0"));
    //TODO: Register Allocation
    instructions.add(new Instruction(InstrType.LABEL, "p_check_divide_by_zero:"));
    //TODO: ADD LR
    instructions.add(new Instruction(InstrType.LABEL, "PUSH {lr}"));
    instructions.add(new Instruction(InstrType.CMP, r1, 0));
    instructions.add(new Instruction(InstrType.LDR, r0, "msg_" + msgNumber, Conditionals.EQ));
    instructions.add(new Instruction(InstrType.BL, "p_throw_runtime_error", Conditionals.EQ));
    instructions.add(new Instruction(InstrType.LABEL, "POP {pc}"));
    runtimeErr = true;
    return instructions;
  }

  @Override
  public List<Instruction> visitProgram(Program program) {
    List<Instruction> instructions = new ArrayList<>();

    //TODO: ADD CONSTRUCTOR FOR DIRECTIVES
    //TODO: Only include this instruction if needed
    instructions.add(new Instruction(InstrType.DATA, ""));
    instructions.add(new Instruction(InstrType.LABEL, "")); // Leave gap in lines

    //TODO: ADD VARIABLE INSTRUCTIONS HERE

    instructions.add(new Instruction(InstrType.TEXT, ""));
    instructions.add(new Instruction(InstrType.LABEL, "")); // Leave gap in lines

    instructions.add(new Instruction(InstrType.GLOBAL_MAIN, ""));

    /* Generate the assembly instructions for each function. */
    for (Function function : program.getFunctions()) {
      instructions.addAll(visitFunction(function));
    }

    //TODO: ADD ENUM FOR LABEL
    instructions.add(new Instruction(InstrType.LABEL, "main:"));

    //TODO: ADD LR
    instructions.add(new Instruction(InstrType.LABEL, "PUSH {lr}"));

    spLocation = totalBytesInProgram(program);
    if (spLocation > 0) {
      instructions.add(new Instruction(InstrType.LABEL, "SUB sp, sp, #" + spLocation));
      //TODO: SUB sp, sp, #spLocation
    }

    currentST = new SymbolTable(null);

    /* Generate the assembly instructions for the program body. */
    instructions.addAll(visitStatement(program.getStatement()));

    spLocation = totalBytesInProgram(program);
    if (spLocation > 0) {
      instructions.add(new Instruction(InstrType.LABEL, "ADD sp, sp, #" + spLocation));
      //TODO: SUB sp, sp, #spLocation
    }

    instructions.add(new Instruction(InstrType.LDR, r0, 0));
    instructions.add(new Instruction(InstrType.LABEL, "POP {pc}"));
    instructions.add(new Instruction(InstrType.LTORG, ""));

    //number of messages
    int msgNumber = 0;

    if(isArrayLookup) {
      instructions = arrayIndexOutOfBoundsError(instructions, msgNumber);
      msgNumber+=2;
    }

    if(isCalc) {
      instructions = throwOverflowError(instructions, msgNumber);
      msgNumber++;
    }

    if(isDiv) {
      instructions = checkDivideByZero(instructions, msgNumber);
      msgNumber++;
    }

    //TODO: Note: runtimeErr must come last
    if(runtimeErr) {
      instructions.add(new Instruction(InstrType.LABEL, "p_throw_runtime_error:"));
      instructions.add(new Instruction(InstrType.BL, "p_print_string"));
      instructions.add(new Instruction(InstrType.MOV, r0, -1));
      instructions.add(new Instruction(InstrType.BL, "exit"));
    }

    return instructions;
  }

  //TODO: ADD FUNCTION PARAMETERS TO SYMBOL TABLE
  @Override
  public List<Instruction> visitFunction(Function function) {
    spLocation = totalBytesInFunction(function);
    return visitStatement(function.getStatement());
  }

  //TODO: ADD RHS VISIT FUNCTION
  @Override
  public List<Instruction> visitDeclarationStatement(Statement statement) {
    return visitExpression(statement.getRHS().getExpression1());
  }

  @Override
  public List<Instruction> visitConcatStatement(Statement statement) {
    List<Instruction> instructions = new ArrayList<>();

    /* Generate the assembly code for each statement in the concatenated statement. */
    instructions.addAll(visitStatement(statement.getStatement1()));
    instructions.addAll(visitStatement(statement.getStatement2()));

    return instructions;
  }

  @Override
  public List<Instruction> visitSkipStatement(Statement statement) {
    /* Generate instructions when a skip statement is found, that is, no instructions. */
    return Collections.emptyList();
  }

  @Override
  public List<Instruction> visitIntLiterExp(Expression expression) {

    /* Allocate a register: rn for this function to use. */
    Register rn = popUnusedRegister();

    List<Instruction> instructions = new ArrayList<>();

    // LDR rn, =i
    instructions.add(new Instruction(InstrType.LDR, rn, expression.getIntLiter()));

    /* Mark the register used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitBoolLiterExp(Expression expression) {

    /* Allocate a register: rn for this function to use. */
    Register rn = popUnusedRegister();

    long boolVal = expression.getBoolLiter() ? 1 : 0;

    List<Instruction> instructions = new ArrayList<>();

    // MOV rn, #(1 | 0)
    instructions.add(new Instruction(InstrType.MOV, rn, boolVal));

    /* Mark the register used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitCharLiterExp(Expression expression) {

    /* Allocate a register: rn for this function to use. */
    Register rn = popUnusedRegister();

    List<Instruction> instructions = new ArrayList<>();

    // MOV rn, #charVal
    //TODO: Make instruction look like: MOV r4, #'x' - as an example
    instructions.add(new Instruction(InstrType.MOV, rn, expression.getCharLiter()));
    //TODO: Ensure that following instruction is "STRB rn, [sp]"

    /* Mark the register used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
  }

  //TODO: How do we know that it will be msg_0, what happens if there are 2 strings
  @Override
  public List<Instruction> visitStringLiterExp(Expression expression) {

    //TODO Generate initial message label
    List<Instruction> instructions = new ArrayList<>();

    /* Allocate a register: rn for this function to use. */
    Register rn = popUnusedRegister();

    // LDR rn, =msg_0
    //TODO: Verify this matches the instruction above
    instructions.add(new Instruction(InstrType.LDR, rn, "msg_0"));

    /* Mark the register used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
  }

  //TODO: VERIFY THAT THIS MANIPULATES THE STACK POINTER PROPERLY
  @Override
  public List<Instruction> visitIdentExp(Expression expression) {
    String expressionIdent = expression.getIdent();
    int storedSPLocation = currentST.getSPMapping(expressionIdent);
    spLocation = spLocation - sizeOfTypeOnStack(currentST.getType(expression.getIdent()));

    return new ArrayList<>(List.of(new Instruction(InstrType.STR, unusedRegisters.get(1), new Operand2(sp))));
  }

  @Override
  public List<Instruction> visitArrayElemExp(Expression expression) {

    isArrayLookup = true;

    List<Instruction> instructions = new ArrayList<>();
    String expressionIdent = expression.getIdent();
    int storedSPLocation = currentST.getSPMapping(expressionIdent);
    spLocation = spLocation - sizeOfTypeOnStack(currentST.getType(expression.getIdent()));
    instructions.add(new Instruction(InstrType.ADD, sp, 0));

    //The index is assumed to be stored at register 5.
    visitExpression(expression.getArrayElem().getExpression().get(0));
    instructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(4),
        new Operand2(unusedRegisters.get(4))));
    instructions.add(new Instruction(InstrType.MOV, unusedRegisters.get(0),
        new Operand2(unusedRegisters.get(5))));
    instructions.add(new Instruction(InstrType.MOV, unusedRegisters.get(1),
        new Operand2(unusedRegisters.get(4))));
    instructions.add(new Instruction(InstrType.BL, "p_check_array_bounds"));
    instructions.add(new Instruction(InstrType.ADD, unusedRegisters.get(4),
        unusedRegisters.get(4), new Operand2(4)));
    instructions.add(new Instruction(InstrType.LABEL, "ADD rn, rn, LSL #2"));
    instructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(4),
        new Operand2(unusedRegisters.get(4))));

    return instructions;
  }

  private List<Instruction> translateUnaryExpression(Expression expression) {
    /* Generate assembly instructions for the expression. */
    return new ArrayList<>(visitExpression(expression.getExpression1()));
  }

  @Override
  public List<Instruction> visitNotExp(Expression expression) {
    List<Instruction> instructions = translateUnaryExpression(expression);

    /* Allocate a register: rn for this function to use. */
    Register rn = popUnusedRegister();

    // EOR rn, rn, #1
    instructions.add(new Instruction(InstrType.EOR, rn, rn, new Operand2(1)));

    /* Mark the register used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitNegExp(Expression expression) {

    isCalc = true;

    List<Instruction> instructions = translateUnaryExpression(expression);

    /* Allocate a register: rn for this function to use. */
    Register rn = popUnusedRegister();
    
    // RSBS rn, rn, #0
    instructions.add(new Instruction(InstrType.RSB, rn, rn, new Operand2(0), Flags.S));

    // BLVS p_throw_overflow_error
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error", Conditionals.VS));

    /* Mark the register used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
  }

  //TODO: Infinitely loops and never computes the length
  @Override
  public List<Instruction> visitLenExp(Expression expression) {
    // LDR r4, [r4]
    return translateUnaryExpression(expression);
  }

  //TODO: Infinitely loops and never computes ord
  @Override
  public List<Instruction> visitOrdExp(Expression expression) {
    // MOV r4, expr
    return translateUnaryExpression(expression);
  }

  //TODO: Infinitely loops and never computes chr
  @Override
  public List<Instruction> visitChrExp(Expression expression) {
    // MOV r4, expr
    return translateUnaryExpression(expression);
  }

  private List<Instruction> translateBinaryExpression(Expression expression) {
    List<Instruction> instructions = new ArrayList<>();

    /* Generate assembly instructions for the first expression. */
    instructions.addAll(visitExpression(expression.getExpression1())); //Store result in Rn

    /* Declare that rn is in use. */
    Register rn = popUnusedRegister();

    /* Generate assembly instructions for the second expression. */
    instructions.addAll(visitExpression(expression.getExpression2())); //Store result in Rn+1

    /* Declare that rn is no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitPlusExp(Expression expression) {

    isCalc = true;

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // ADDS Rn, Rn, Rn+1
    instructions.add(new Instruction(InstrType.ADD, rn, rn, new Operand2(rm), Flags.S));

    // BLVS p_throw_overflow_error
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error", Conditionals.VS));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitMinusExp(Expression expression) {

    isCalc = true;

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // SUBS Rn, Rn, Rn+1
    instructions.add(new Instruction(InstrType.SUB, rn, rn, new Operand2(rm), Flags.S));

    //TODO: Add VS condition code
    // BLVS p_throw_overflow_error
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error", Conditionals.VS));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitMulExp(Expression expression) {

    isCalc = true;

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // SMULL rn, rm, rn, rm
    instructions.add(new Instruction(InstrType.SMULL, rn, rm, rn, rm));

    // CMP Rn+1, Rn, ASR #31
    instructions.add(new Instruction(InstrType.LABEL, "CMP " + rm + ", " + rn + ", ASR #31"));

    // BLNE p_throw_overflow_error
    //TODO: Add NE condition code
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error", Conditionals.NE));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitDivExp(Expression expression) {

    //Set isDiv to True for visitProgram
    isDiv = true;

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // MOV R0, Rn
    instructions.add(new Instruction(InstrType.MOV, r0, new Operand2(rn)));

    // MOV R1, Rn+1
    instructions.add(new Instruction(InstrType.MOV, r1, new Operand2(rm)));

    // BL p_check_divide_by_zero
    instructions.add(new Instruction(InstrType.BL, "p_check_divide_by_zero"));

    // BL __aeabi_idiv
    instructions.add(new Instruction(InstrType.BL, "__aeabi_idiv"));

    // MOV Rn, R0
    instructions.add(new Instruction(InstrType.MOV, rn, new Operand2(r0)));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitModExp(Expression expression) {

    //Set isDiv to True for visitProgram
    isDiv = true;

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // MOV R0, Rn
    instructions.add(new Instruction(InstrType.MOV, r0, new Operand2(rn)));

    // MOV R1, Rn+1
    instructions.add(new Instruction(InstrType.MOV, r1, new Operand2(rm)));

    // BL p_check_divide_by_zero
    instructions.add(new Instruction(InstrType.BL, "p_check_divide_by_zero"));

    // BL __aeabi_idiv
    instructions.add(new Instruction(InstrType.BL, "__aeabi_idivmod"));

    // MOV Rn, R1
    instructions.add(new Instruction(InstrType.MOV, rn, new Operand2(r1)));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitGreaterExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, rn, new Operand2(rm)));

    // MOVLE Rn, #0
    instructions.add(new Instruction(InstrType.MOV, rn, 0));

    // MOVGT Rn, #1
    //TODO: CREATE CONDITION CODE ENUMS
    instructions.add(new Instruction(InstrType.MOV, rn, 1));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitGreaterEqExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, rn, new Operand2(rm)));

    // MOVLT Rn, #0
    instructions.add(new Instruction(InstrType.MOV, rn, 0));

    // MOVGE Rn, #1
    //TODO: CREATE CONDITION CODE ENUMS
    instructions.add(new Instruction(InstrType.MOV, rn, 1));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitLessExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, rn, new Operand2(rm)));

    // MOVGE Rn, #0
    instructions.add(new Instruction(InstrType.MOV, rn, 0));

    // MOVLT Rn, #1
    //TODO: CREATE CONDITION CODE ENUMS
    instructions.add(new Instruction(InstrType.MOV, rn, 1));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitLessEqExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, rn, new Operand2(rm)));

    // MOVGT Rn, #0
    instructions.add(new Instruction(InstrType.MOV, rn, 0));

    // MOVLE Rn, #1
    //TODO: CREATE CONDITION CODE ENUMS
    instructions.add(new Instruction(InstrType.MOV, rn, 1));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  //TODO
  @Override
  public List<Instruction> visitEqExp(Expression expression) {
    return super.visitEqExp(expression);
  }

  //TODO
  @Override
  public List<Instruction> visitNeqExp(Expression expression) {
    return super.visitNeqExp(expression);
  }

  @Override
  public List<Instruction> visitExprRHS(AssignRHS rhs) {
    return super.visitExprRHS(rhs);
  }

  @Override
  public List<Instruction> visitArrayRHS(AssignRHS rhs) {
    return super.visitArrayRHS(rhs);
  }

  @Override
  public List<Instruction> visitNewPairRHS(AssignRHS rhs) {
    return super.visitNewPairRHS(rhs);
  }

  @Override
  public List<Instruction> visitCallRHS(AssignRHS rhs) {
    return super.visitCallRHS(rhs);
  }
}
