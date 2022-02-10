package ast;

import java.util.List;

public class Function {

  private final Type returnType;
  private final String ident;
  private final List<Param> params;
  private final Statement statement;

  public Function(Type returnType, String ident, List<Param> params, Statement statement) {
    this.returnType = returnType;
    this.ident = ident;
    this.params = params;
    this.statement = statement;
  }

  public Type getReturnType() {
    return returnType;
  }

  public String getIdent() {
    return ident;
  }

  public List<Param> getParams() {
    return params;
  }

  public Statement getStatement() {
    return statement;
  }

  @Override
  public String toString() {
    return "Function{" +
            "returnType=" + returnType +
            ", ident='" + ident + '\'' +
            ", params=" + params +
            ", statement=" + statement +
            '}';
  }

}
