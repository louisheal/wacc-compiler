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

  enum ExprType {

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
