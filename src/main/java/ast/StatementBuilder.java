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

  public StatementBuilder withStatType(Statement.StatType statType) {
    this.statType = statType;
    return this;
  }

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
    return new Statement(statType);
  }

  public Statement buildDeclaration() {
    return new Statement(statType, lhsType, lhsIdent, rhs);
  }

  public Statement buildReassignment() {
    return new Statement(statType, lhs, rhs);
  }

  public Statement buildRead() {
    return new Statement(statType, lhs);
  }

  public Statement buildFree() {
    return new Statement(statType, expression);
  }

  public Statement buildReturn() {
    return new Statement(statType, expression);
  }

  public Statement buildExit() {
    return new Statement(statType, expression);
  }

  public Statement buildPrint() {
    return new Statement(statType, expression);
  }

  public Statement buildPrintln() {
    return new Statement(statType, expression);
  }

  public Statement buildIfThenElseFi() {
    return new Statement(statType, expression, statement1, statement2);
  }

  public Statement buildWhile() {
    return new Statement(statType, expression, statement1);
  }

  public Statement buildBegin() {
    return new Statement(statType, statement1);
  }

  public Statement buildSemiColon() {
    return new Statement(statType, statement1, statement2);
  }



}
