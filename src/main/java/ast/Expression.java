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

  public Expression(ExprType exprType, int intLiter) {
    this.exprType = exprType;
    this.intLiter = intLiter;
  }

  @Override
  public String toString() {

    StringBuilder result = new StringBuilder();

    if (exprType == ExprType.INTLITER) {
      result.append(intLiter);
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
