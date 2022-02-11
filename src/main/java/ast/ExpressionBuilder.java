package ast;

public class ExpressionBuilder {

  private Expression.ExprType exprType;
  private int intLiter;
  private boolean boolLiter;
  private char charLiter;
  private String stringLiter;
  private String ident;
  private ArrayElem arrayElem;
  private Expression expression1;
  private Expression expression2;

  public Expression buildIntExpr(int intLiter) {
    this.exprType = Expression.ExprType.INTLITER;
    this.intLiter = intLiter;
    return this.build();
  }

  private Expression build() {
    return new Expression(exprType, intLiter, boolLiter, charLiter, stringLiter, ident, arrayElem, expression1, expression2);
  }

}
