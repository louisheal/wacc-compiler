import ast.AssignRHS;
import ast.Expression;
import ast.Function;
import ast.Param;
import ast.Program;
import ast.Statement;
import ast.Type;
import java.util.List;

public class TraverseAST {
  SymbolTable currentST = new SymbolTable(null);

  private int errors = 0;

  public Integer getNumberOfErrors() {
    return errors;
  }

  public void traverse(Program program) {
    for (Function function : program.getFunctions()) {
      traverse(function);
    }
    traverse(program.getStatement());
  }

  private void traverse(Function function) {
    traverse(function.getParams());
    traverse(function.getStatement());
  }

  private void traverse(List<Param> params) {

  }

  private void traverse(Expression expression) {

  }

  private void traverse(Statement statement) {
    switch (statement.getStatType()) {
      case SKIP:
        break;
      case DECLARATION:
        currentST.newSymbol(statement.getLhsIdent(), statement.getLhsType().getType());
        traverse(statement.getRHS().getExpression1());
        traverse(statement.getRHS().getExpression2());
        break;
      case REASSIGNMENT:
        traverse(statement.getRHS().getExpression1());
        traverse(statement.getRHS().getExpression2());
        break;
      case READ:
        break;
      case FREE:
        traverse(statement.getExpression());
        break;
      case RETURN:
        traverse(statement.getExpression());
        break;
      case EXIT:
        traverse(statement.getExpression());
        break;
      case PRINT:
        traverse(statement.getExpression());
        break;
      case PRINTLN:
        traverse(statement.getExpression());
        break;
      case IF:
        traverse(statement.getExpression());
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
        break;
      case WHILE:
        traverse(statement.getExpression());
        traverse(statement.getStatement1());
        break;
      case BEGIN:
        traverse(statement.getStatement1());
        break;
      case CONCAT:
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
    }
  }
}
