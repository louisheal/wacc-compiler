import assembly.Instruction;
import assembly.Instruction.InstrType;
import assembly.Operand2;
import assembly.Register;
import ast.Expression;
import ast.Function;
import ast.Program;
import ast.Statement;

import ast.Type;
import ast.Type.EType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Converter extends ASTVisitor<List<Instruction>> {

  List<Instruction> instructions = new ArrayList<>();

  //TODO: ONLY USE FOLLOWING REGISTERS FOR EVALUATION: 4,5,6,7,8,9,10,11
  List<Register> generalRegisters = initialiseGeneralRegisters();
  private Register sp = new Register(13);
  private Register pc = new Register(15);
  SymbolTable currentST;

  private List<Instruction> getInstructionFromExpression(Expression expr) {

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
        return visitIntLiterExp(expr);

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
        return visitBoolLiterExp(expr);

      case CHARLITER:
      case CHR:
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
            pairInstructions.add(new Instruction(InstrType.LDR, generalRegisters.get(0),
                calculateMallocSize(expr, currentST.getType(expr.getIdent()))));
            pairInstructions.add(new Instruction(InstrType.BL, "malloc"));
            pairInstructions.addAll(getInstructionFromExpression(expr.getExpression1()));
            pairInstructions.addAll(getInstructionFromExpression(expr.getExpression2()));
            return pairInstructions;
          case ARRAY:
            List<Instruction> arrayInstructions = new ArrayList<>();
            arrayInstructions.add(new Instruction(InstrType.LDR, generalRegisters.get(0),
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
            arrayInstructions.add(new Instruction(InstrType.LDR, generalRegisters.get(0),
                8L * expr.getArrayElem().getExpression().size()));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element: expr.getArrayElem().getExpression()){
              arrayInstructions.addAll(visitIntLiterExp(element));
            }
            return arrayInstructions;
          case BOOL:
            arrayInstructions.add(new Instruction(InstrType.LDR, generalRegisters.get(0),
                expr.getArrayElem().getExpression().size()));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element: expr.getArrayElem().getExpression()){
              arrayInstructions.addAll(visitBoolLiterExp(element));
            }
            return arrayInstructions;
          case CHAR:
            arrayInstructions.add(new Instruction(InstrType.LDR, generalRegisters.get(0),
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
            arrayInstructions.add(new Instruction(InstrType.LDR, generalRegisters.get(0), charCount));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element: expr.getArrayElem().getExpression()){
              arrayInstructions.addAll(visitStringLiterExp(element));
            }
            return arrayInstructions;
          case PAIR:
            long pairMallocSize = 0;

            for (Expression element : expr.getArrayElem().getExpression()) {
              pairMallocSize += calculateMallocSize(element,
                  currentST.getType(expr.getArrayElem().getIdent()));
            }

            arrayInstructions.add(new Instruction(InstrType.LDR, generalRegisters.get(0), pairMallocSize));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element : expr.getArrayElem().getExpression()) {
              arrayInstructions.addAll(getInstructionFromExpression(element.getExpression1()));
              arrayInstructions.addAll(getInstructionFromExpression(element.getExpression2()));
            }
            return arrayInstructions;
          case ARRAY:
            long arrayMallocSize = 0;

            for (Expression element : expr.getArrayElem().getExpression()) {
              pairMallocSize += calculateMallocSize(element,
                  currentST.getType(expr.getArrayElem().getIdent()));
            }

            arrayInstructions.add(new Instruction(InstrType.LDR, generalRegisters.get(0), arrayMallocSize));
            arrayInstructions.add(new Instruction(InstrType.BL, "malloc"));
            for (Expression element : expr.getArrayElem().getExpression()) {
              arrayInstructions.addAll(getInstructionFromExpression(element));
            }
            return arrayInstructions;
        }

      case BRACKETS:
        return getInstructionFromExpression(expr.getExpression1());

    }
    return null;
  }

  private long calculateMallocSize(Expression exp, Type type){
    switch(type.getType()){
      case INT:
        return 8;

      case BOOL:
      case CHAR:
        return 1;

      case STRING:
        return exp.getStringLiter().length();

      case PAIR:
        return calculateMallocSize(exp, type.getFstType()) + calculateMallocSize(exp,
            type.getSndType());

      case ARRAY:
        long size = 0;
        for (Expression arrayExpression : exp.getArrayElem().getExpression()){
          size += calculateMallocSize(arrayExpression, type.getArrayType());
        }
        return size;
    }
    return 0;

  }



  private List<Register> initialiseGeneralRegisters(){
    List<Register> regs = new ArrayList<>();
    for(int i = 0; i != 13; i++){
      regs.add(new Register(i));
    }
    return regs;
  }

  @Override
  public List<Instruction> visitProgram(Program program) {
    List<Instruction> instructions = new ArrayList<>();

    //TODO: ADD CONSTRUCTOR FOR DIRECTIVES
    instructions.add(new Instruction(InstrType.DATA, ""));

    //TODO: ADD VARIABLE INSTRUCTIONS HERE

    instructions.add(new Instruction(InstrType.TEXT, ""));

    instructions.add(new Instruction(InstrType.GLOBAL_MAIN, ""));

    /* Generate the assembly instructions for each function. */
    for (Function function : program.getFunctions()) {
      instructions.addAll(visitFunction(function));
    }

    //TODO: ADD ENUM FOR LABEL
    instructions.add(new Instruction(InstrType.DATA, "main"));

    /* Generate the assembly instructions for the program body. */
    instructions.addAll(visitStatement(program.getStatement()));

    instructions.add(new Instruction(InstrType.LTORG, ""));

    return instructions;
  }

  //TODO: ADD FUNCTION PARAMETERS TO SYMBOL TABLE
  @Override
  public List<Instruction> visitFunction(Function function) {
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
    return new ArrayList<>(List.of(new Instruction(InstrType.MOV, generalRegisters.get(2),
        expression.getIntLiter())));
  }

  @Override
  public List<Instruction> visitBoolLiterExp(Expression expression) {
    long boolVal = expression.getBoolLiter() ? 1 : 0;
    return new ArrayList<>(List.of(new Instruction(InstrType.MOV, generalRegisters.get(2), boolVal)));
  }

  @Override
  public List<Instruction> visitCharLiterExp(Expression expression) {
    long charVal = Character.getNumericValue(expression.getCharLiter());
    return new ArrayList<>(List.of(new Instruction(InstrType.MOV, generalRegisters.get(2), charVal)));
  }

  @Override
  public List<Instruction> visitStringLiterExp(Expression expression) {
    //TODO Generate initial message label
    List<Instruction> instructions = new ArrayList<>();
      instructions.add(new Instruction(InstrType.LDR, generalRegisters.get(1), "msg_0" ));
      instructions.add(new Instruction(InstrType.PUSH, generalRegisters.get(1)));
    return instructions;
  }

  @Override
  public List<Instruction> visitIdentExp(Expression expression) {
    return getInstructionFromExpression(expression);
  }

  @Override
  public List<Instruction> visitArrayElemExp(Expression expression) {
    return getInstructionFromExpression(expression);
  }

  private List<Instruction> translateUnaryExpression(Expression expression) {
    List<Instruction> instructions = new ArrayList<>();

    /* Generate assembly instructions for the expression. */
    instructions.addAll(visitExpression(expression.getExpression1())); //Store result in Rn

    return instructions;
  }

  @Override
  public List<Instruction> visitNegExp(Expression expression) {
    List<Instruction> instructions = translateUnaryExpression(expression);

    Register dest = generalRegisters.get(4);

    //NEG dest
    instructions.add(new Instruction(InstrType.NEG, dest));

    return instructions;
  }

  @Override
  public List<Instruction> visitLenExp(Expression expression) {
    List<Instruction> instructions = translateUnaryExpression(expression);

    Register dest = generalRegisters.get(4);

    //NEG dest
    instructions.add(new Instruction(InstrType.LEN, dest));

    return instructions;
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

    Register rn = generalRegisters.get(4);
    Register rm = generalRegisters.get(5);

    //TODO: Add {S} condition code
    //ADDS Rn, Rn, Rn+1
    instructions.add(new Instruction(InstrType.ADD, rn, rn, new Operand2(rm)));

    //TODO: Add VS condition code
    //BLVS p_throw_overflow_error
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error"));

    return instructions;
  }

  @Override
  public List<Instruction> visitMinusExp(Expression expression) {
    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    Register rn = generalRegisters.get(4);
    Register rm = generalRegisters.get(5);

    //TODO: Add {S} condition code
    //SUBS Rn, Rn, Rn+1
    instructions.add(new Instruction(InstrType.SUB, rn, rn, new Operand2(rm)));

    //TODO: Add VS condition code
    //BLVS p_throw_overflow_error
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error"));

    return instructions;
  }

  @Override
  public List<Instruction> visitMulExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    // SMULL Rn, Rn+1, Rn, Rn+1
    Register rn = generalRegisters.get(1);
    Register rm = generalRegisters.get(2);
    instructions.add(new Instruction(InstrType.SMULL, rn, rm, rn, rm));

    // CMP Rn+1, Rn, ASR #31

    // BLNE p_throw_overflow_error
    //TODO: Add NE condition code
    instructions.add(new Instruction(InstrType.BL, "p_throw_overflow_error"));

    return instructions;
  }

  //TODO: MAKE R1 AND R0 RESERVED REGISTERS FOR FUNCTION CALLS
  @Override
  public List<Instruction> visitDivExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    // SMULL Rn, Rn+1, Rn, Rn+1
    Register rn = generalRegisters.get(4);
    Register rm = generalRegisters.get(5);

    // MOV R0, Rn
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(0), new Operand2(rn)));

    // MOV R1, Rn+1
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), new Operand2(rm)));

    // BL p_check_divide_by_zero
    instructions.add(new Instruction(InstrType.BL, "p_check_divide_by_zero"));

    // BL __aeabi_idiv
    instructions.add(new Instruction(InstrType.BL, "__aeabi_idiv"));

    // MOV Rn, R0
    instructions.add(new Instruction(InstrType.MOV, rn, new Operand2(generalRegisters.get(0))));

    return instructions;
  }

  @Override
  public List<Instruction> visitGreaterExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, generalRegisters.get(1),
        new Operand2(generalRegisters.get(2))));

    // MOVLE Rn, #0
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), 0));

    // MOVGT Rn, #1
    //TODO: CREATE CONDITION CODE ENUMS
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), 1));

    return instructions;
  }

  @Override
  public List<Instruction> visitGreaterEqExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, generalRegisters.get(1),
        new Operand2(generalRegisters.get(2))));

    // MOVLT Rn, #0
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), 0));

    // MOVGE Rn, #1
    //TODO: CREATE CONDITION CODE ENUMS
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), 1));

    return instructions;
  }

  @Override
  public List<Instruction> visitLessExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, generalRegisters.get(1),
        new Operand2(generalRegisters.get(2))));

    // MOVGE Rn, #0
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), 0));

    // MOVLT Rn, #1
    //TODO: CREATE CONDITION CODE ENUMS
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), 1));

    return instructions;
  }

  @Override
  public List<Instruction> visitLessEqExp(Expression expression) {

    /* Generate assembly code to evaluate both expressions and store them in Rn, Rn+1. */
    List<Instruction> instructions = translateBinaryExpression(expression);

    // CMP Rn, Rn+1
    instructions.add(new Instruction(InstrType.CMP, generalRegisters.get(1),
        new Operand2(generalRegisters.get(2))));

    // MOVGT Rn, #0
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), 0));

    // MOVLE Rn, #1
    //TODO: CREATE CONDITION CODE ENUMS
    instructions.add(new Instruction(InstrType.MOV, generalRegisters.get(1), 1));

    return instructions;
  }
}
