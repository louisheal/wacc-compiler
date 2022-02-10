package ast;

public class Statement {

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

