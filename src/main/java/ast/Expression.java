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
      result.append(boolLiter);
    }

    if (exprType == ExprType.CHARLITER) {
      result.append("'").append(charLiter).append("'");
    }

    return result.toString().toLowerCase();
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
    UN_MINUS,
    LEN,
    ORD,
    CHR,
    DIVIDE,
    MULTIPLY,
    MODULO,
    PLUS,
    BIN_MINUS,
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
