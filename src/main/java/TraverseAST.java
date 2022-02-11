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

  private void traverse(Statement statement) {
    switch (statement.getStatType()) {
      case SKIP:
        break;
      case DECLARATION:
        currentST.newSymbol(statement.getLhsIdent(), statement.getLhsType().getType());
      case REASSIGNMENT:
        Type.EType lhsType = currentST.getType(statement.getLhsIdent());
        statement.getRHS();
        // need to check if rhs has same type as lhs

      case READ:

      case FREE:

      case RETURN:

      case EXIT:

      case PRINT:

      case PRINTLN:

      case IF:

      case WHILE:

      case BEGIN:

      case CONCAT:
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
    }
  }
}
