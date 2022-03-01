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

  //TODO: DELETE?
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

  //TODO: DELETE?
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

  private Type getExpressionType(Expression expr) {

    if (expr == null) {
      return null;
    }

    switch (expr.getExprType()) {

      case INTLITER:
      case NEG:
      case ORD:
      case LEN:
      case DIVIDE:
      case MULTIPLY:
      case MODULO:
      case PLUS:
      case MINUS:
        return new Type(Type.EType.INT);

      case BOOLLITER:
      case NOT:
      case GT:
      case GTE:
      case LT:
      case LTE:
      case EQ:
      case NEQ:
      case AND:
      case OR:
        return new Type(Type.EType.BOOL);

      case CHARLITER:
      case CHR:
        return new Type(Type.EType.CHAR);

      case STRINGLITER:
        return new Type(Type.EType.STRING);

      case IDENT:
        return currentST.getType(expr.getIdent());

      case ARRAYELEM:
        return currentST.getType(expr.getArrayElem().getIdent()).getArrayType();

      case BRACKETS:
        return getExpressionType(expr.getExpression1());

    }
    return null;
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
      return totalBytesInScope(statement.getStatement1()) + maxBeginStatement(statement);
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

  @Override
  public List<Instruction> visitSkipStatement(Statement statement) {
    /* Generate instructions when a skip statement is found, that is, no instructions. */
    return Collections.emptyList();
  }

  //TODO: Add variable to symbol table
  @Override
  public List<Instruction> visitDeclarationStatement(Statement statement) {

    /* Generate instructions to evaluate the RHS and put the result into the first unused register. */
    List<Instruction> instructions = new ArrayList<>(visitRHS(statement.getRHS()));

    /* Retrieve the first register which is where the value of the RHS is stored. */
    Register rn = popUnusedRegister();

    int stackOffset = spLocation - sizeOfTypeOnStack(statement.getLhsType());

    String instruction;
    if (stackOffset > 0) {
      // STR rn, [sp]
      instruction = String.format("STR %s, [sp, #%d]", rn, stackOffset);
    } else {
      // STR rn, [sp, #i]
      instruction = String.format("STR %s, [sp]", rn);
    }
    instructions.add(new Instruction(InstrType.LABEL, instruction));

    /* Add the variable to the symbol table. */
    currentST.setSPMapping(statement.getLhsIdent(), stackOffset);
    currentST.newVariable(statement.getLhsIdent(), statement.getLhsType());

    /* Update the spLocation variable to point to the next available space on the stack. */
    spLocation = stackOffset;

    /* Mark the register used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
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

  @Override
  public List<Instruction> visitIdentExp(Expression expression) {

    List<Instruction> instructions = new ArrayList<>();

    /* Retrieve the position of the variable on the stack from the symbol table. */
    int stackOffset = currentST.getSPMapping(expression.getIdent());

    /* Allocate a register: rn for this function to use. */
    Register rn = popUnusedRegister();

    String instruction;
    if (stackOffset != 0) {
      instruction = String.format("LDR %s, [sp, #%d]", rn, stackOffset);
    } else {
      instruction = String.format("LDR %s, [sp]", rn);
    }
    instructions.add(new Instruction(InstrType.LABEL, instruction));

    /* Mark the register used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rn);

    return instructions;
  }

  // TODO: Multi-dimensional array lookup
  @Override
  public List<Instruction> visitArrayElemExp(Expression expression) {

    /* Set ArrayLookup flag to true. */
    isArrayLookup = true;

    ArrayElem arrayElem = expression.getArrayElem();
    List<Instruction> instructions = new ArrayList<>();
    String instruction;

    /* Find where the array address is stored on the stack. */
    int stackOffset = currentST.getSPMapping(expression.getIdent());

    /* Allocate one register: rn for this function to use. */
    Register rn = popUnusedRegister();

    // ADD rn, sp, #offset
    instruction = String.format("ADD %s, sp, #%d", rn, stackOffset);
    instructions.add(new Instruction(InstrType.LABEL, instruction));

    /* Evaluate the index of the ArrayElem and store it in rm. */
    instructions.addAll(visitExpression(arrayElem.getExpression().get(0)));

    /* Retrieve the register rm which contains the value of the index. */
    Register rm = popUnusedRegister();

    // LDR rn, [rn]
    instructions.add(new Instruction(InstrType.LDR, rn, new Operand2(rn)));

    // MOV r1, rn
    instructions.add(new Instruction(InstrType.MOV, r1, new Operand2(rn)));

    // BL p_check_array_bounds
    instructions.add(new Instruction(InstrType.BL, "p_check_array_bounds"));

    // ADD rn, rn, #4
    instructions.add(new Instruction(InstrType.ADD, rn, rn, new Operand2(4)));

    // ADD rn, rn, rm, LSL #2
    instruction = String.format("ADD %s, %s, LSL #2", rn, rn);
    instructions.add(new Instruction(InstrType.LABEL, instruction));

    // LDR rn, [rn]
    instructions.add(new Instruction(InstrType.LDR, rn, new Operand2(rn)));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

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
    instructions.add(new Instruction(InstrType.MOV, rn, 0, Conditionals.LE));

    // MOVGT Rn, #1
    instructions.add(new Instruction(InstrType.MOV, rn, 1, Conditionals.GT));

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
    instructions.add(new Instruction(InstrType.MOV, rn, 0, Conditionals.LT));

    // MOVGE Rn, #1
    instructions.add(new Instruction(InstrType.MOV, rn, 1, Conditionals.GE));

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
    instructions.add(new Instruction(InstrType.MOV, rn, 0, Conditionals.GE));

    // MOVLT Rn, #1
    instructions.add(new Instruction(InstrType.MOV, rn, 1, Conditionals.LT));

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
    instructions.add(new Instruction(InstrType.MOV, rn, 0, Conditionals.GT));

    // MOVLE Rn, #1
    instructions.add(new Instruction(InstrType.MOV, rn, 1, Conditionals.LE));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitEqExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, rn, new Operand2(rm)));

    // MOVEQ Rn, #1
    instructions.add(new Instruction(InstrType.MOV, rn, 1, Conditionals.EQ));

    // MOVNE Rn, #0
    instructions.add(new Instruction(InstrType.MOV, rn, 0, Conditionals.NE));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitNeqExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    /* Allocate two registers: rn and rm (rn+1) for this function to use. */
    Register rn = popUnusedRegister();
    Register rm = popUnusedRegister();

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, rn, new Operand2(rm)));

    // MOVNE Rn, #1
    instructions.add(new Instruction(InstrType.MOV, rn, 1, Conditionals.NE));

    // MOVEQ Rn, #0
    instructions.add(new Instruction(InstrType.MOV, rn, 0, Conditionals.EQ));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  //TODO
  @Override
  public List<Instruction> visitAndExp(Expression expression) {
    return super.visitAndExp(expression);
  }

  //TODO
  @Override
  public List<Instruction> visitOrExp(Expression expression) {
    return super.visitOrExp(expression);
  }

  @Override
  public List<Instruction> visitExprRHS(AssignRHS rhs) {
    return visitExpression(rhs.getExpression1());
  }

  @Override
  public List<Instruction> visitArrayRHS(AssignRHS rhs) {

    List<Expression> array = rhs.getArray();
    List<Instruction> instructions = new ArrayList<>();

    int mallocSize = 4;
    for (Expression expression : array) {
      //TODO: private Type expressionToType(Expression expression){}
      mallocSize += sizeOfTypeOnStack(getExpressionType(expression));
    }

    // LDR r0, =mallocSize
    instructions.add(new Instruction(InstrType.LDR, r0, mallocSize));

    // BL malloc
    instructions.add(new Instruction(InstrType.BL, "malloc"));

    /* Allocate one register: rn for this function to use. */
    Register rn = popUnusedRegister();

    // MOV rn, r0
    instructions.add(new Instruction(InstrType.MOV, rn, new Operand2(r0)));

    int offset = mallocSize;
    for (Expression expression : array) {

      /* Generate instructions to evaluate each expression. */
      instructions.addAll(visitExpression(expression));

      /* Retrieve the register containing the evaluated expression. */
      Register rm = popUnusedRegister();

      /* Store the evaluated expression into the malloc location at an offset. */
      instructions.add(new Instruction(InstrType.LABEL, String.format("STR %s, [%s, #%d]", rm, rn, offset)));

      /* Mark register rm as no longer in use. */
      pushUnusedRegister(rm);
    }

    /* Allocate a register: rm for this function to use. */
    Register rm = popUnusedRegister();

    // LDR rm, =array.size()
    instructions.add(new Instruction(InstrType.LDR, rm, array.size()));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitNewPairRHS(AssignRHS rhs) {

    List<Instruction> instructions = new ArrayList<>();
    Register rn, rm;

    // LDR r0, =8
    /* Loads the value 8 into register 0, as every pair is 8 bytes on the heap. */
    instructions.add(new Instruction(InstrType.LDR, r0, 8));

    // BL malloc
    instructions.add(new Instruction(InstrType.BL, "malloc"));

    /* Allocate one register: rn for this function to use. */
    rn = popUnusedRegister();

    // MOV rn, r0
    instructions.add(new Instruction(InstrType.MOV, rn, new Operand2(r0)));

    /* Evaluate the first expression in the pair. */
    instructions.addAll(visitExpression(rhs.getExpression1()));

    /* Retrieve the register containing the value of the first expression. */
    rm = popUnusedRegister();

    int sizeOfExp1 = sizeOfTypeOnStack(getExpressionType(rhs.getExpression1()));

    // LDR r0, =sizeOfType
    instructions.add(new Instruction(InstrType.LDR, r0, sizeOfExp1));

    // BL malloc
    instructions.add(new Instruction(InstrType.BL, "malloc"));

    // STR rm, [r0]
    instructions.add(new Instruction(InstrType.STR, r0, new Operand2(rm)));

    // STR r0, [rn]
    instructions.add(new Instruction(InstrType.STR, rn, new Operand2(r0)));

    /* Mark the register rm as no longer in use. */
    pushUnusedRegister(rm);

    /* Evaluate the second expression in the pair. */
    instructions.addAll(visitExpression(rhs.getExpression2()));

    /* Retrieve the register containing the value of the first expression. */
    rm = popUnusedRegister();

    // LDR r0, =sizeOfType
    instructions.add(new Instruction(InstrType.LDR, r0, sizeOfTypeOnStack(getExpressionType(rhs.getExpression2()))));

    // BL malloc
    instructions.add(new Instruction(InstrType.BL, "malloc"));

    // STR rm, [r0]
    instructions.add(new Instruction(InstrType.STR, r0, new Operand2(rm)));

    // STR r0, [rn, #sizeOfExp1]
    instructions.add(new Instruction(InstrType.LABEL, String.format("STR r0, [%s, #%d]", rn, sizeOfExp1)));

    /* Mark the two registers used in the evaluation of this function as no longer in use. */
    pushUnusedRegister(rm);
    pushUnusedRegister(rn);

    return instructions;
  }

  @Override
  public List<Instruction> visitCallRHS(AssignRHS rhs) {
    return super.visitCallRHS(rhs);
  }
}
