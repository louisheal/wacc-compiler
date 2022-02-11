import ast.Function;
import ast.Program;
import ast.Statement;

public class TraverseAST {

  public void traverse(Program program) {
    for (Function function : program.getFunctions()) {
      traverse(function);
    }
    traverse(program.getStatement());
  }

  private void traverse(Function function) {

  }

  private void traverse(Statement statement) {

  }

}
