import ast.ArrayElem;
import ast.AssignLHS;
import ast.AssignRHS;
import ast.Expression;
import ast.ExpressionBuilder;
import ast.Function;
import ast.Program;
import ast.Statement;
import java.util.Objects;

public class Evaluator extends ASTVisitor<Expression> {

  @Override
  public Expression visitProgram(Program program) {

    for (Function function : program.getFunctions()) {
      visitFunction(function);
    }
    visitStatement(program.getStatement());
    return null;
  }

  @Override
  public Expression visitFunction(Function function) {
    visitStatement(function.getStatement());
    return null;
  }

  @Override
  public Expression visitDeclarationStatement(Statement statement) {
    visitRHS(statement.getRHS());
    return null;
  }

  @Override
  public Expression visitReassignmentStatement(Statement statement) {
    visitRHS(statement.getRHS());
    return null;
  }

  @Override
  public Expression visitFreeStatement(Statement statement) {
    Expression expression = statement.getExpression();
    expression.setExpression(visitExpression(expression));
    return null;
  }

  @Override
  public Expression visitReturnStatement(Statement statement) {
    Expression expression = statement.getExpression();
    expression.setExpression(visitExpression(expression));
    return null;
  }

  @Override
  public Expression visitExitStatement(Statement statement) {
    Expression expression = statement.getExpression();
    expression.setExpression(visitExpression(expression));
    return null;
  }

  @Override
  public Expression visitPrintStatement(Statement statement) {
    Expression expression = statement.getExpression();
    expression.setExpression(visitExpression(expression));
    return null;
  }

  @Override
  public Expression visitPrintlnStatement(Statement statement) {
    Expression expression = statement.getExpression();
    expression.setExpression(visitExpression(expression));
    return null;
  }

  @Override
  public Expression visitIfStatement(Statement statement) {
    Expression expression = statement.getExpression();
    expression.setExpression(visitExpression(expression));
    visitStatement(statement.getStatement1());
    visitStatement(statement.getStatement2());
    return null;
  }

  @Override
  public Expression visitWhileStatement(Statement statement) {
    Expression expression = statement.getExpression();
    expression.setExpression(visitExpression(expression));
    visitStatement(statement.getStatement1());
    return null;
  }

  @Override
  public Expression visitBeginStatement(Statement statement) {
    visitStatement(statement.getStatement1());
    return null;
  }

  @Override
  public Expression visitIntLiterExp(Expression expression) {
    return expression;
  }

  @Override
  public Expression visitBoolLiterExp(Expression expression) {
    return expression;
  }

  @Override
  public Expression visitCharLiterExp(Expression expression) {
    return expression;
  }

  @Override
  public Expression visitStringLiterExp(Expression expression) {
    return expression;
  }

  @Override
  public Expression visitIdentExp(Expression expression) {
    return expression;
  }

  @Override
  public Expression visitArrayElemExp(Expression expression) {
    ArrayElem arrayElem = expression.getArrayElem();
    for (Expression exp : arrayElem.getExpression()) {
      exp.setExpression(visitExpression(exp));
    }
    return null;
  }

  @Override
  public Expression visitNotExp(Expression expression) {
    boolean bool = visitExpression(expression.getExpression1()).getBoolLiter();
    return new ExpressionBuilder().buildBoolExpr(!bool);
  }

  @Override
  public Expression visitNegExp(Expression expression) {
    long integer = visitExpression(expression.getExpression1()).getIntLiter();
    return new ExpressionBuilder().buildIntExpr(-integer);
  }

  @Override
  public Expression visitLenExp(Expression expression) {
    Expression exp = expression.getExpression1();
    exp.setExpression(visitExpression(exp));
    return expression;
  }

  @Override
  public Expression visitOrdExp(Expression expression) {
    long ord = visitExpression(expression.getExpression1()).getCharLiter();
    return new ExpressionBuilder().buildIntExpr(ord);
  }

  @Override
  public Expression visitChrExp(Expression expression) {
    char chr = (char) visitExpression(expression.getExpression1()).getIntLiter();
    return new ExpressionBuilder().buildCharExpr(chr);
  }

  @Override
  public Expression visitDivExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildIntExpr(arg1 / arg2);
  }

  @Override
  public Expression visitMulExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildIntExpr(arg1 * arg2);
  }

  @Override
  public Expression visitModExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildIntExpr(arg1 % arg2);
  }

  @Override
  public Expression visitPlusExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildIntExpr(arg1 + arg2);
  }

  @Override
  public Expression visitMinusExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildIntExpr(arg1 - arg2);
  }

  @Override
  public Expression visitGreaterExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildBoolExpr(arg1 > arg2);
  }

  @Override
  public Expression visitGreaterEqExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildBoolExpr(arg1 >= arg2);
  }

  @Override
  public Expression visitLessExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildBoolExpr(arg1 < arg2);
  }

  @Override
  public Expression visitLessEqExp(Expression expression) {
    long arg1 = visitExpression(expression.getExpression1()).getIntLiter();
    long arg2 = visitExpression(expression.getExpression2()).getIntLiter();
    return new ExpressionBuilder().buildBoolExpr(arg1 <= arg2);
  }

  @Override
  public Expression visitEqExp(Expression expression) {
    boolean bool1 = visitExpression(expression.getExpression1()).getBoolLiter();
    boolean bool2 = visitExpression(expression.getExpression2()).getBoolLiter();
    return new ExpressionBuilder().buildBoolExpr(Objects.equals(bool1,bool2));
  }

  @Override
  public Expression visitNeqExp(Expression expression) {
    boolean bool1 = visitExpression(expression.getExpression1()).getBoolLiter();
    boolean bool2 = visitExpression(expression.getExpression2()).getBoolLiter();
    return new ExpressionBuilder().buildBoolExpr(!Objects.equals(bool1,bool2));
  }

  @Override
  public Expression visitAndExp(Expression expression) {
    boolean bool1 = visitExpression(expression.getExpression1()).getBoolLiter();
    boolean bool2 = visitExpression(expression.getExpression2()).getBoolLiter();
    return new ExpressionBuilder().buildBoolExpr(bool1 && bool2);
  }

  @Override
  public Expression visitOrExp(Expression expression) {
    boolean bool1 = visitExpression(expression.getExpression1()).getBoolLiter();
    boolean bool2 = visitExpression(expression.getExpression2()).getBoolLiter();
    return new ExpressionBuilder().buildBoolExpr(bool1 || bool2);
  }

  @Override
  public Expression visitExprRHS(AssignRHS rhs) {
    Expression expression = rhs.getExpression1();
    expression.setExpression(visitExpression(expression));
    return null;
  }

  @Override
  public Expression visitNewPairRHS(AssignRHS rhs) {
    Expression expression1 = rhs.getExpression1();
    Expression expression2 = rhs.getExpression2();
    expression1.setExpression(visitExpression(expression1));
    expression2.setExpression(visitExpression(expression2));
    return null;
  }

  @Override
  public Expression visitArrayElemLHS(AssignLHS lhs) {
    ArrayElem arrayElem = lhs.getArrayElem();
    return visitExpression(new ExpressionBuilder().buildArrayExpr(arrayElem));
  }

  @Override
  public Expression visitPairElemLHS(AssignLHS lhs) {
    Expression expression = lhs.getPairElem().getExpression();
    expression.setExpression(visitExpression(expression));
    return null;
  }
}
