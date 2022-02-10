package ast;

public class PairElem {

  private final PairElemType type;
  private final Expression expression;

  public PairElem(PairElemType type, Expression expression) {
    this.type = type;
    this.expression = expression;
  }

  public PairElemType getType() {
    return type;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    return type + " " + expression;
  }

  public enum PairElemType {

    FST,
    SND

  }

}
