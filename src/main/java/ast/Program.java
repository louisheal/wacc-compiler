package ast;

import java.util.List;

public class Program {

  private final List<Function> functions;
  private final Statement statement;

  public Program(List<Function> functions, Statement statement) {
    this.functions = functions;
    this.statement = statement;
  }

  public List<Function> getFunctions() {
    return functions;
  }

  public Statement getStatement() {
    return statement;
  }

  @Override
  public String toString() {
    return "Program{" +
            "functions=" + functions +
            ", statement=" + statement +
            '}';
  }
}
