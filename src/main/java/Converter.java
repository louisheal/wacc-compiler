import antlr.BasicParser;
import assembly.Instruction;
import assembly.Instruction.InstrType;
import assembly.Register;
import ast.Expression;
import ast.Function;
import ast.Program;
import ast.Statement;
import ast.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// This class will be used to generate assembly code from WACC code
public class Converter extends ASTVisitor<List<Instruction>>{

  List<Instruction> instructions = new ArrayList<>();

  private Register r0 = new Register(0);
  private Register r1 = new Register(1);
  private Register r2 = new Register(2);
  private Register r3 = new Register(3);
  private Register r4 = new Register(4);
  private Register r5 = new Register(5);
  private Register r6 = new Register(6);
  private Register r7 = new Register(7);
  private Register r8 = new Register(8);
  private Register r9 = new Register(9);
  private Register r10 = new Register(10);
  private Register r11 = new Register(11);
  private Register r12 = new Register(12);
  private Register sp = new Register(13);
  private Register pc = new Register(15);

  public void translateDeclaration(Statement statement) {

  }

  //TODO: Change to ASTVisitor<List<Instruction>>

  @Override
  public List<Instruction> visitProgram(Program program) {
    List<Instruction> functionInstructions = new ArrayList<>();

    /* Generate the assembly instructions for each function. */
    for (Function function : program.getFunctions()) {
      functionInstructions.addAll(visitFunction(function));
    }

    /* Generate the assembly instructions for the program body. */
    List<Instruction> statementInstructions = visitStatement(program.getStatement());

      /* Return the function assembly instructions concatenated with the assembly instructions
         for the program body. */
    return Stream.of(functionInstructions, statementInstructions)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  //TODO: ADD FUNCTION PARAMETERS TO SYMBOL TABLE
  @Override
  public List<Instruction> visitFunction(Function function) {
    return visitStatement(function.getStatement());
  }

  @Override
  public List<Instruction> visitConcatStatement(Statement statement) {
      /* Generate, concatenate and return the assembly instructions for both statements either
         side of the semicolon */
    return Stream.of(visitStatement(statement.getStatement1()),
            visitStatement(statement.getStatement2()))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public List<Instruction> visitSkipStatement(Statement statement) {
    /* Generate instructions when a skip statement is found, that is, no instructions */
    return Collections.emptyList();
  }

  @Override
  public List<Instruction> visitIntLiterExp(Expression expression) {
    List<Instruction> instructionList = new ArrayList<>();
    instructionList.add(new Instruction(
        InstrType.MOV
        , new Register(2)
        , Math.toIntExact(expression.getIntLiter())
    ));
    return instructionList;
  }





}
