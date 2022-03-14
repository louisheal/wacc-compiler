package ast;

import java.util.List;

public class Function {

  private final Type returnType;
  private String ident;
  private final List<Param> params;
  private final Statement statement;

  public Function(Type returnType, String ident, List<Param> params, Statement statement) {
    this.returnType = returnType;
    this.params = params;
    this.ident = ident;
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

  public void setIdent(String ident) {
    this.ident = ident;
  }

  @Override
  public String toString() {
    StringBuilder paramSB = new StringBuilder();
    for (Param param : params) {
      paramSB.append(param.getType()).append(" ").append(param.getIdent()).append(" ");
    }
    return returnType + " " + ident + " (" + paramSB + ") is\n" + statement + "end\n";
  }

}
