package ast;

public class Expression {

  ExprType exprType;
  int intLiter;
  boolean boolLiter;
  char charLiter;
  String stringLiter;
  // PAIR
  String ident;
  ArrayElem arrayElem;
  Expression expression1;
  Expression expression2;

  public Expression(ExprType exprType, int intLiter, boolean boolLiter, char charLiter, String stringLiter,
                    String ident, ArrayElem arrayElem, Expression expression1, Expression expression2) {
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

    return result.toString();
  }

  public enum ExprType {

    INTLITER,
    BOOLLITER,
    CHARLITER,
    STRINGLITER,
    PAIRLITER,
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
