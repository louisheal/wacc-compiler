package ast;

import java.util.Objects;

public class Type {

  private final EType type;
  private final Type arrayType;
  private final EType fstType;
  private final EType sndType;

  public Type(EType type) {
    this.type = type;
    this.arrayType = null;
    this.fstType = null;
    this.sndType = null;
  }

  public Type(EType type, Type arrayType) {
    this.type = type;
    this.arrayType = arrayType;
    this.fstType = null;
    this.sndType = null;
  }

  public EType getType() {
    return type;
  }

  public Type getArrayType() {
    return arrayType;
  }

  public EType getFstType() {
    return fstType;
  }

  public EType getSndType() {
    return sndType;
  }

  @Override
  public String toString() {
    String result;
    if (type == EType.ARRAY) {
      result = arrayType + "[]";
    } else if (type == EType.PAIR) {
      result = "(" + fstType + ", " + sndType + ")";
    } else {
      result = type.toString();
    }

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Type type1 = (Type) o;
    return type == type1.type && Objects.equals(arrayType, type1.arrayType) && fstType == type1.fstType && sndType == type1.sndType;
  }

  public enum EType {

    INT,
    BOOL,
    CHAR,
    STRING,
    ARRAY,
    PAIR

  }

}
