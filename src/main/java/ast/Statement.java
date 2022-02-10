package ast;

public class Statement {

  StatType statType;
  Type lhsType;
  String lhsIdent;
  AssignLHS lhs;
  AssignRHS rhs;
  Expression expression;
  Statement statement1;
  Statement statement2;

  enum StatType {

    SKIP,
    DECLARATION,
    REASSIGNMENT,
    READ,
    FREE,
    RETURN,
    EXIT,
    PRINT,
    PRINTLN,
    IF,
    WHILE,
    BEGIN,
    CONCAT

  }

}

