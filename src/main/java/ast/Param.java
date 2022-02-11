package ast;

public class Param {

  Type type;
  String ident;

  public Param(Type type, String ident) {
    this.type = type;
    this.ident = ident;
  }

  public Type getType() {
    return type;
  }

  public String getIdent() {
    return ident;
  }

  @Override
  public String toString() {
    return type + ", " + ident;
  }

}
