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
    this.statType = Statement.StatType.SKIP;
    return this.build();
  }

  public Statement buildDeclaration(Type lhsType, String lhsIdent, AssignRHS rhs) {
    this.statType = Statement.StatType.DECLARATION;
    this.lhsType = lhsType;
    this.lhsIdent = lhsIdent;
    this.rhs = rhs;
    return this.build();
  }

  public Statement buildReassignment(AssignLHS lhs, AssignRHS rhs) {
    this.statType = Statement.StatType.REASSIGNMENT;
    this.lhs = lhs;
    this.rhs = rhs;
    return this.build();
  }

  public Statement buildRead(AssignLHS lhs) {
    this.statType = Statement.StatType.READ;
    this.lhs = lhs;
    return this.build();
  }

  public Statement buildFree(Expression expression) {
    this.statType = Statement.StatType.FREE;
    this.expression = expression;
    return this.build();
  }

  public Statement buildReturn(Expression expression) {
    this.statType = Statement.StatType.RETURN;
    this.expression = expression;
    return this.build();
  }

  public Statement buildExit(Expression expression) {
    this.statType = Statement.StatType.EXIT;
    this.expression = expression;
    return this.build();
  }

  public Statement buildPrint(Expression expression) {
    this.statType = Statement.StatType.PRINT;
    this.expression = expression;
    return this.build();
  }

  public Statement buildPrintln(Expression expression) {
    this.statType = Statement.StatType.PRINTLN;
    this.expression = expression;
    return this.build();
  }

  public Statement buildIfThenElse(Expression expression, Statement statement1, Statement statement2) {
    this.statType = Statement.StatType.IF;
    this.expression = expression;
    this.statement1 = statement1;
    this.statement2 = statement2;
    return this.build();
  }

  public Statement buildWhile(Expression expression, Statement statement1) {
    this.statType = Statement.StatType.WHILE;
    this.expression = expression;
    this.statement1 = statement1;
    return this.build();
  }

  public Statement buildBegin(Statement statement1) {
    this.statType = Statement.StatType.BEGIN;
    this.statement1 = statement1;
    return this.build();
  }

  public Statement buildSemiColon(Statement statement1, Statement statement2) {
    this.statType = Statement.StatType.CONCAT;
    this.statement1 = statement1;
    this.statement2 = statement2;
    return this.build();
  }

  public Statement build() {
    return new Statement(statType, lhsType, lhsIdent, lhs, rhs, expression, statement1, statement2);
  }


}
