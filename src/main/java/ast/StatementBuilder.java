package ast;

public class StatementBuilder {
  private Statement.StatType statType;
  private Type lhsType;
  private String lhsIdent;
  private AssignLHS lhs;
  private AssignRHS rhs;
  private Expression expression;
  private Statement statement1;
  private Statement statement2;

  public StatementBuilder withLHSType(Type lhsType) {
    this.lhsType = lhsType;
    return this;
  }

  public StatementBuilder withLHSIdent(String lhsIdent) {
    this.lhsIdent = lhsIdent;
    return this;
  }

  public StatementBuilder withLHS(AssignLHS lhs) {
    this.lhs = lhs;
    return this;
  }

  public StatementBuilder withRHS(AssignRHS rhs) {
    this.rhs = rhs;
    return this;
  }

  public StatementBuilder withExpression(Expression expression) {
    this.expression = expression;
    return this;
  }

  public StatementBuilder withStatement1(Statement statement1) {
    this.statement1 = statement1;
    return this;
  }

  public StatementBuilder withStatement2(Statement statement2) {
    this.statement2 = statement2;
    return this;
  }

  public Statement buildSkip() {
    return new Statement(Statement.StatType.SKIP);
  }

  public Statement buildDeclaration() {
    return new Statement(Statement.StatType.DECLARATION, lhsType, lhsIdent, rhs);
  }

  public Statement buildReassignment() {
    return new Statement(Statement.StatType.REASSIGNMENT, lhs, rhs);
  }

  public Statement buildRead() {
    return new Statement(Statement.StatType.READ, lhs);
  }

  public Statement buildFree() {
    return new Statement(Statement.StatType.FREE, expression);
  }

  public Statement buildReturn() {
    return new Statement(Statement.StatType.RETURN, expression);
  }

  public Statement buildExit() {
    return new Statement(Statement.StatType.EXIT, expression);
  }

  public Statement buildPrint() {
    return new Statement(Statement.StatType.PRINT, expression);
  }

  public Statement buildPrintln() {
    return new Statement(Statement.StatType.PRINTLN, expression);
  }

  public Statement buildIfThenElse() {
    return new Statement(Statement.StatType.IF, expression, statement1, statement2);
  }

  public Statement buildWhile() {
    return new Statement(Statement.StatType.WHILE, expression, statement1);
  }

  public Statement buildBegin() {
    return new Statement(Statement.StatType.BEGIN, statement1);
  }

  public Statement buildSemiColon() {
    return new Statement(Statement.StatType.CONCAT, statement1, statement2);
  }



}
