package ast;

public class Statement {

  private final StatType statType;
  private final Type lhsType;
  private final String lhsIdent;
  private final AssignLHS lhs;
  private final AssignRHS rhs;
  private final Expression expression;
  private final Statement statement1;
  private final Statement statement2;

  public Statement(StatType statType, Type lhsType, String lhsIdent, AssignLHS lhs, AssignRHS rhs,
                   Expression expression, Statement statement1, Statement statement2) {
    this.statType = statType;
    this.lhsType = lhsType;
    this.lhsIdent = lhsIdent;
    this.lhs = lhs;
    this.rhs = rhs;
    this.expression = expression;
    this.statement1 = statement1;
    this.statement2 = statement2;
  }

  public Statement(StatType statType) {
    this.statType = statType;
    this.lhsType = null;
    this.lhsIdent = null;
    this.lhs = null;
    this.rhs = null;
    this.expression = null;
    this.statement1 = null;
    this.statement2 = null;
  }

  public Statement(StatType statType, Type lhsType, String lhsIdent, AssignRHS rhs) {
    this.statType = statType;
    this.lhsType = lhsType;
    this.lhsIdent = lhsIdent;
    this.lhs = null;
    this.rhs = rhs;
    this.expression = null;
    this.statement1 = null;
    this.statement2 = null;
  }

  public Statement(StatType statType, AssignLHS lhs, AssignRHS rhs) {
    this.statType = statType;
    this.lhsType = null;
    this.lhsIdent = null;
    this.lhs = lhs;
    this.rhs = rhs;
    this.expression = null;
    this.statement1 = null;
    this.statement2 = null;
  }

  public Statement(StatType statType, AssignLHS lhs) {
    this.statType = statType;
    this.lhsType = null;
    this.lhsIdent = null;
    this.lhs = lhs;
    this.rhs = null;
    this.expression = null;
    this.statement1 = null;
    this.statement2 = null;
  }

  public Statement(StatType statType, Expression expression) {
    this.statType = statType;
    this.lhsType = null;
    this.lhsIdent = null;
    this.lhs = null;
    this.rhs = null;
    this.expression = expression;
    this.statement1 = null;
    this.statement2 = null;
  }

  public Statement(StatType statType, Expression expression, Statement statement1, Statement statement2) {
    this.statType = statType;
    this.lhsType = null;
    this.lhsIdent = null;
    this.lhs = null;
    this.rhs = null;
    this.expression = expression;
    this.statement1 = statement1;
    this.statement2 = statement2;
  }

  public Statement(StatType statType, Expression expression, Statement statement) {
    this.statType = statType;
    this.lhsType = null;
    this.lhsIdent = null;
    this.lhs = null;
    this.rhs = null;
    this.expression = expression;
    this.statement1 = statement;
    this.statement2 = null;
  }

  public Statement(StatType statType, Statement statement) {
    this.statType = statType;
    this.lhsType = null;
    this.lhsIdent = null;
    this.lhs = null;
    this.rhs = null;
    this.expression = null;
    this.statement1 = statement;
    this.statement2 = null;
  }

  public Statement(StatType statType, Statement statement1, Statement statement2) {
    this.statType = statType;
    this.lhsType = null;
    this.lhsIdent = null;
    this.lhs = null;
    this.rhs = null;
    this.expression = null;
    this.statement1 = statement1;
    this.statement2 = statement2;
  }

  public StatType getStatType() {
    return statType;
  }

  public Type getLhsType() {
    return lhsType;
  }

  public String getLhsIdent() {
    return lhsIdent;
  }

  public AssignLHS getLHS() {
    return lhs;
  }

  public AssignRHS getRHS() {
    return rhs;
  }

  public Expression getExpression() {
    return expression;
  }

  public Statement getStatement1() {
    return statement1;
  }

  public Statement getStatement2() {
    return statement2;
  }

  @Override
  public String toString() {

    StringBuilder result = new StringBuilder();

    if (statType != StatType.CONCAT) {
      result.append(statType).append(": ");
    }

    if (statType == StatType.DECLARATION) {
      result.append(lhsType).append(" ").append(lhsIdent).append(" = ").append(rhs);
    }

    if (statType == StatType.REASSIGNMENT) {
      result.append(lhs).append(" = ").append(rhs);
    }

    if (statType == StatType.READ) {
      result.append(statType.toString().toLowerCase()).append(" ").append(lhs);
    }

    if (statType == StatType.FREE ||
        statType == StatType.RETURN ||
        statType == StatType.EXIT ||
        statType == StatType.PRINT ||
        statType == StatType.PRINTLN) {
      result.append(statType.toString().toLowerCase()).append(" ").append(expression);
    }

    if (statType == StatType.IF) {
      result.append("if ").append(expression).append(" then\n").append(statement1)
              .append(" else\n").append(statement2).append("fi");
    }

    if (statType == StatType.WHILE) {
      result.append("while ").append(expression).append(" do ").append(statement1);
    }

    if (statType == StatType.BEGIN) {
      result.append("begin\n").append(statement1);
    }

    if (statType == StatType.CONCAT) {
      result.append(statement1).append(statement2);
    } else {
      result.append('\n');
    }

    return result.toString();
  }

  public enum StatType {

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

