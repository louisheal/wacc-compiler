package ast;

public class Type {

  EType type;
  Type arrayType;
  EType fstType;
  EType sndType;

  enum EType {

    INT,
    BOOL,
    CHAR,
    STRING,
    ARRAY,
    PAIR

  }

}
