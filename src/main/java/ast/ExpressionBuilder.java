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

  public Expression buildBoolExpr(boolean boolLiter) {
    this.exprType = Expression.ExprType.BOOLLITER;
    this.boolLiter = boolLiter;
    return this.build();
  }

  public Expression buildCharExpr(char charLiter) {
    this.exprType = Expression.ExprType.CHARLITER;
    this.charLiter = charLiter;
    return this.build();
  }

  public Expression buildIdentExpr(String ident) {
    this.exprType = Expression.ExprType.IDENT;
    this.ident = ident;
    return this.build();
  }

  public Expression buildArrayExpr(ArrayElem arrayElem) {
    this.exprType = Expression.ExprType.ARRAYELEM;
    this.arrayElem = arrayElem;
    return this.build();
  }

  //TODO: UnOp Expressions

  //TODO: IntOp Expressions

  //TODO: IntCharOpExpressions

  //TODO: AllOpExpressions

  //TODO: BoolOpExpressions

  public Expression buildBracketsExpr(Expression expression) {
    this.exprType = Expression.ExprType.BRACKETS;
    this.expression1 = expression;
    return this.build();
  }

  private Expression build() {
    return new Expression(exprType, intLiter, boolLiter, charLiter, stringLiter, ident, arrayElem, expression1, expression2);
  }

}
