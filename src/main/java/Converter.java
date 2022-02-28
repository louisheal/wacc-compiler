import assembly.Conditionals;
import assembly.Flags;
import assembly.Instruction;
import assembly.Instruction.InstrType;
import assembly.Operand2;
import assembly.Register;
import ast.Expression;
import ast.Function;
import ast.Program;
import ast.Statement;

import ast.Type;
import java.util.*;

public class Converter extends ASTVisitor<List<Instruction>> {

  //TODO: ONLY USE FOLLOWING REGISTERS FOR EVALUATION: 4,5,6,7,8,9,10,11
  List<Register> unusedRegisters = initialiseGeneralRegisters();

  /* Function return registers. */
  private final Register r0 = new Register(0);
  private final Register r1 = new Register(1);

  /* Special registers. */
  private final Register sp = new Register(13);
  private final Register pc = new Register(15);

  private int spLocation = 0;
  SymbolTable currentST;

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

    /* Generate the assembly instructions for the program body. */
    instructions.addAll(visitStatement(program.getStatement()));

    spLocation = totalBytesInProgram(program);
    if (spLocation > 0) {
      instructions.add(new Instruction(InstrType.LABEL, "ADD sp, sp, #" + spLocation));
      //TODO: SUB sp, sp, #spLocation
    }

    instructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(0), 0));
    instructions.add(new Instruction(InstrType.LABEL, "POP {pc}"));
    instructions.add(new Instruction(InstrType.LTORG, ""));

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
    return new ArrayList<>(List.of(new Instruction(InstrType.MOV, unusedRegisters.get(2),
        expression.getIntLiter())));
  }

  @Override
  public List<Instruction> visitBoolLiterExp(Expression expression) {
    long boolVal = expression.getBoolLiter() ? 1 : 0;
    return new ArrayList<>(List.of(new Instruction(InstrType.MOV, unusedRegisters.get(2), boolVal)));
  }

  @Override
  public List<Instruction> visitCharLiterExp(Expression expression) {
    long charVal = Character.getNumericValue(expression.getCharLiter());
    return new ArrayList<>(List.of(new Instruction(InstrType.MOV, unusedRegisters.get(2), charVal)));
  }

  @Override
  public List<Instruction> visitStringLiterExp(Expression expression) {
    //TODO Generate initial message label
    List<Instruction> instructions = new ArrayList<>();
      instructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(1), "msg_0" ));
      instructions.add(new Instruction(InstrType.PUSH, unusedRegisters.get(1)));
    return instructions;
  }

  @Override
  public List<Instruction> visitIdentExp(Expression expression) {
    String expressionIdent = expression.getIdent();
    int storedSPLocation = currentST.getSPMapping(expressionIdent);
    spLocation = spLocation - sizeOfTypeOnStack(currentST.getType(expression.getIdent()));

    return new ArrayList<>(List.of(new Instruction(InstrType.STR, unusedRegisters.get(1), new Operand2(sp))));
  }

  @Override
  public List<Instruction> visitArrayElemExp(Expression expression) {
    List<Instruction> instructions = new ArrayList<>();
    String expressionIdent = expression.getIdent();
    int storedSPLocation = currentST.getSPMapping(expressionIdent);
    spLocation = spLocation - sizeOfTypeOnStack(currentST.getType(expression.getIdent()));
    instructions.add(new Instruction(InstrType.ADD, sp, 0));
    //The index is assumed to be stored at register 5.
    visitExpression(expression.getArrayElem().getExpression().get(0));
    instructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(4),
        new Operand2(unusedRegisters.get(4))));
    instructions.add(new Instruction(InstrType.LDR, unusedRegisters.get(4),
        new Operand2(unusedRegisters.get(4))));
    instructions.add(new Instruction(InstrType.MOV, unusedRegisters.get(0),
        new Operand2(unusedRegisters.get(5))));
    instructions.add(new Instruction(InstrType.MOV, unusedRegisters.get(1),
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

    Register dest = unusedRegisters.get(4);

    //EOR r4, r4, #1
    instructions.add(new Instruction(InstrType.EOR, dest, dest, new Operand2(1)));

    return instructions;
  }

  @Override
  public List<Instruction> visitNegExp(Expression expression) {
    List<Instruction> instructions = translateUnaryExpression(expression);

    Register dest = unusedRegisters.get(4);
    
    //RSBS r4, r4, #0
    instructions.add(new Instruction(InstrType.RSB, dest, dest, new Operand2(0), Flags.S));

    //BLVS p_throw_overflow_error
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error", Conditionals.VS));

    return instructions;
  }

  //TODO: Infinitely loops and never computes the length
  @Override
  public List<Instruction> visitLenExp(Expression expression) {
    //LDR r4, [r4]
    return translateUnaryExpression(expression);
  }

  //TODO: Infinitely loops and never computes ord
  @Override
  public List<Instruction> visitOrdExp(Expression expression) {
    //MOV r4, expr
    return translateUnaryExpression(expression);
  }

  //TODO: Infinitely loops and never computes chr
  @Override
  public List<Instruction> visitChrExp(Expression expression) {
    //MOV r4, expr
    return translateUnaryExpression(expression);
  }

  private List<Instruction> translateBinaryExpression(Expression expression) {
    List<Instruction> instructions = new ArrayList<>();

    /* Generate assembly instructions for the first expression. */
    instructions.addAll(visitExpression(expression.getExpression1())); //Store result in Rn

    /* Generate assembly instructions for the second expression. */
    instructions.addAll(visitExpression(expression.getExpression2())); //Store result in Rn+1

    return instructions;
  }

  @Override
  public List<Instruction> visitPlusExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    Register rn = unusedRegisters.get(4);
    Register rm = unusedRegisters.get(5);

    //ADDS Rn, Rn, Rn+1
    instructions.add(new Instruction(InstrType.ADD, rn, rn, new Operand2(rm), Flags.S));

    //BLVS p_throw_overflow_error
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error", Conditionals.VS));

    return instructions;
  }

  @Override
  public List<Instruction> visitMinusExp(Expression expression) {
    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    Register rn = unusedRegisters.get(4);
    Register rm = unusedRegisters.get(5);

    //SUBS Rn, Rn, Rn+1
    instructions.add(new Instruction(InstrType.SUB, rn, rn, new Operand2(rm), Flags.S));

    //TODO: Add VS condition code
    //BLVS p_throw_overflow_error
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error", Conditionals.VS));

    return instructions;
  }

  @Override
  public List<Instruction> visitMulExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    // SMULL Rn, Rn+1, Rn, Rn+1
    Register rn = unusedRegisters.get(1);
    Register rm = unusedRegisters.get(2);
    instructions.add(new Instruction(InstrType.SMULL, rn, rm, rn, rm));

    // CMP Rn+1, Rn, ASR #31

    // BLNE p_throw_overflow_error
    //TODO: Add NE condition code
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error", Conditionals.NE));

    return instructions;
  }

  //TODO: MAKE R1 AND R0 RESERVED REGISTERS FOR FUNCTION CALLS
  @Override
  public List<Instruction> visitDivExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    // SMULL Rn, Rn+1, Rn, Rn+1
    Register rn = unusedRegisters.get(4);
    Register rm = unusedRegisters.get(5);

    // MOV R0, Rn
    instructions.add(new Instruction(InstrType.MOV, unusedRegisters.get(0), new Operand2(rn)));

    // MOV R1, Rn+1
    instructions.add(new Instruction(InstrType.MOV, unusedRegisters.get(1), new Operand2(rm)));

    // BL p_check_divide_by_zero
    instructions.add(new Instruction(InstrType.BL, "p_check_divide_by_zero"));

    // BL __aeabi_idiv
    instructions.add(new Instruction(InstrType.BL, "__aeabi_idiv"));

    // MOV Rn, R0
    instructions.add(new Instruction(InstrType.MOV, rn, new Operand2(unusedRegisters.get(0))));

    return instructions;
  }

  @Override
  public List<Instruction> visitModExp(Expression expression) {

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
}
