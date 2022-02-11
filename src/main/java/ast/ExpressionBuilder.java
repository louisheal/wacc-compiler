package ast;

import javax.print.DocFlavor;

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

  public Expression buildStringExpr(String stringLiter) {
    this.exprType = Expression.ExprType.STRINGLITER;
    this.stringLiter = stringLiter;
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

  public Expression buildUnOpExpr(Expression.ExprType exprType, Expression expression) {
    this.exprType = exprType;
    this.expression1 = expression;
    return this.build();
  }

  public Expression buildDivExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.DIVIDE;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildMulExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.MULTIPLY;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildModExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.MODULO;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildPlusExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.PLUS;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildMinusExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.MINUS;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildGtExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.GT;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildGteExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.GTE;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildLtExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.LT;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildLteExpr(Expression expression1, Expression expression2) {
    this.exprType = Expression.ExprType.LTE;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public Expression buildBracketsExpr(Expression expression) {
    this.exprType = Expression.ExprType.BRACKETS;
    this.expression1 = expression;
    return this.build();
  }

  private Expression build() {
    return new Expression(exprType, intLiter, boolLiter, charLiter, stringLiter, ident, arrayElem, expression1, expression2);
  }

}
