package ast;

import java.util.List;

public class ArrayElem {

  String ident;
  List<Expression> expressions;

  public ArrayElem(String ident, List<Expression> expressions) {
    this.ident = ident;
    this.expressions = expressions;
  }

  public String getIdent() {
    return ident;
  }

  public List<Expression> getExpression() {
    return expressions;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(ident);
    for (Expression expression : expressions) {
      result.append("[").append(expression).append("]");
    }
    return result.toString();
  }
}
