package ast;

import java.util.Objects;

public class Expression {

  private ExprType exprType;
  private long intLiter;
  private boolean boolLiter;
  private char charLiter;
  private String stringLiter;
  private String ident;
  private ArrayElem arrayElem;
  private Expression expression1;
  private Expression expression2;

  public Expression(ExprType exprType, long intLiter, boolean boolLiter, char charLiter, String stringLiter
                    ,String ident, ArrayElem arrayElem, Expression expression1,
      Expression expression2) {
    this.exprType = exprType;
    this.intLiter = intLiter;
    this.boolLiter = boolLiter;
    this.charLiter = charLiter;
    this.stringLiter = stringLiter;
    this.ident = ident;
    this.arrayElem = arrayElem;
    this.expression1 = expression1;
    this.expression2 = expression2;
  }

  @Override
  public String toString() {

    StringBuilder result = new StringBuilder();

    if (exprType == ExprType.INTLITER) {
      result.append(intLiter);
    }

    if (exprType == ExprType.BOOLLITER) {
      result.append(String.valueOf(boolLiter).toLowerCase());
    }

    if (exprType == ExprType.CHARLITER) {
      result.append("'").append(charLiter).append("'");
    }

    if (exprType == ExprType.STRINGLITER) {
      result.append(stringLiter);
    }

    if (exprType == ExprType.IDENT) {
      result.append(ident);
    }

    if (exprType == ExprType.ARRAYELEM) {
      result.append(arrayElem);
    }

    if (exprType == ExprType.NOT) {
      result.append("!").append(expression1);
    }

    if (exprType == ExprType.NEG) {
      result.append("-").append(expression1);
    }

    if (exprType == ExprType.LEN) {
      result.append("len ").append(expression1);
    }

    if (exprType == ExprType.ORD) {
      result.append("ord ").append(expression1);
    }

    if (exprType == ExprType.CHR) {
      result.append("chr ").append(expression1);
    }

    if (exprType == ExprType.DIVIDE) {
      result.append(expression1).append(" / ").append(expression2);
    }

    if (exprType == ExprType.MULTIPLY) {
      result.append(expression1).append(" * ").append(expression2);
    }

    if (exprType == ExprType.MODULO) {
      result.append(expression1).append(" % ").append(expression2);
    }

    if (exprType == ExprType.PLUS) {
      result.append(expression1).append(" + ").append(expression2);
    }

    if (exprType == ExprType.MINUS) {
      result.append(expression1).append(" - ").append(expression2);
    }

    if (exprType == ExprType.GT) {
      result.append(expression1).append(" > ").append(expression2);
    }

    if (exprType == ExprType.GTE) {
      result.append(expression1).append(" >= ").append(expression2);
    }

    if (exprType == ExprType.LT) {
      result.append(expression1).append(" < ").append(expression2);
    }

    if (exprType == ExprType.LTE) {
      result.append(expression1).append(" <= ").append(expression2);
    }

    if (exprType == ExprType.EQ) {
      result.append(expression1).append(" == ").append(expression2);
    }

    if (exprType == ExprType.NEQ) {
      result.append(expression1).append(" != ").append(expression2);
    }

    if (exprType == ExprType.AND) {
      result.append(expression1).append(" && ").append(expression2);
    }

    if (exprType == ExprType.OR) {
      result.append(expression1).append(" || ").append(expression2);
    }

    if (exprType == ExprType.BRACKETS) {
      result.append("(").append(expression1).append(")");
    }

    return result.toString();
  }

  public long getIntLiter() {
    return intLiter;
  }

  public boolean getBoolLiter() {
    return boolLiter;
  }

  public char getCharLiter() {
    return charLiter;
  }

  public String getStringLiter() {
    return stringLiter;
  }

  public ExprType getExprType() {
    return exprType;
  }

  public Expression getExpression1() {
    return expression1;
  }

  public Expression getExpression2() {
    return expression2;
  }

  public ArrayElem getArrayElem(){
    return arrayElem;
  }

  public String getIdent() {
    return ident;
  }

  public void setExpression(Expression expression) {
    this.exprType = expression.exprType;
    this.intLiter = expression.intLiter;
    this.boolLiter = expression.boolLiter;
    this.charLiter = expression.charLiter;
    this.stringLiter = expression.stringLiter;
    this.ident = expression.ident;
    this.arrayElem = expression.arrayElem;
    this.expression1 = expression.expression1;
    this.expression2 = expression.expression2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Expression)) {
      return false;
    }
    Expression that = (Expression) o;

    return (exprType == ExprType.INTLITER && that.exprType == ExprType.INTLITER && intLiter == that.intLiter) ||
        (exprType == ExprType.BOOLLITER && that.exprType == ExprType.BOOLLITER && boolLiter == that.boolLiter);

//    return intLiter == that.intLiter && boolLiter == that.boolLiter &&
//        charLiter == that.charLiter &&
//        exprType == that.exprType && Objects.equals(stringLiter, that.stringLiter) &&
//        Objects.equals(ident, that.ident) &&
//        Objects.equals(arrayElem, that.arrayElem) &&
//        Objects.equals(expression1, that.expression1) &&
//        Objects.equals(expression2, that.expression2);
  }


  public enum ExprType {

    INTLITER,
    BOOLLITER,
    CHARLITER,
    STRINGLITER,
    IDENT,
    ARRAYELEM,
    NOT,
    NEG,
    LEN,
    ORD,
    CHR,
    DIVIDE,
    MULTIPLY,
    MODULO,
    PLUS,
    MINUS,
    GT,
    GTE,
    LT,
    LTE,
    EQ,
    NEQ,
    AND,
    OR,
    BRACKETS

  }

}
