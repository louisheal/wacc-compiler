package ast;

import java.util.List;

public class Function {

  private final Type returnType;
  private final String ident;
  private final List<Param> params;
  private final Statement statement;

  public Function(Type returnType, String ident, List<Param> params, Statement statement) {
    this.returnType = returnType;
    this.params = params;
    this.ident = ident + getParamTypes();
    this.statement = statement;
  }

  public Type getReturnType() {
    return returnType;
  }

  public String getIdent() {
    return ident;
  }

  public String getParamTypes() {
    if (params.size() == 0) {
      return "";
    }
    StringBuilder indentWithTypes = new StringBuilder();
    indentWithTypes.append("_");
    for (Param param : params) {
      indentWithTypes.append(param.getType()).append("_");
    }
    return indentWithTypes.substring(0, indentWithTypes.toString().length() - 1);
  }

  public List<Param> getParams() {
    return params;
  }

  public Statement getStatement() {
    return statement;
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
