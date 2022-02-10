import antlr.BasicParser;
import antlr.BasicParserBaseVisitor;
import ast.*;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends BasicParserBaseVisitor<Object> {

  @Override
  public Program visitProg(BasicParser.ProgContext ctx) {

    List<Function> functions = new ArrayList<>();
    Statement statement = (Statement) this.visit(ctx.stat());

    for (int i = 0; i < ctx.func().size(); i++) {
      functions.add((Function) this.visit(ctx.func(i)));
    }

    return new Program(functions, statement);
  }

  @Override
  public Function visitFunc(BasicParser.FuncContext ctx) {
    Type returnType = (Type) this.visit(ctx.type());
    String ident = ctx.IDENT().getText();
    List<Param> params = visitParamList(ctx.paramList());
    Statement statement = (Statement) this.visit(ctx.stat());

    return new Function(returnType, ident, params, statement);
  }

  @Override
  public Statement visitSkip(BasicParser.SkipContext ctx) {
    return new Statement(Statement.StatType.SKIP);
  }

  @Override
  public Statement visitDeclaration(BasicParser.DeclarationContext ctx) {
    Type type = (Type) this.visit(ctx.type());
    String ident = (String) this.visit(ctx.IDENT());
    AssignRHS rhs = (AssignRHS) this.visit(ctx.assignRHS());

    return new Statement(Statement.StatType.DECLARATION, type, ident, rhs);
  }

  @Override
  public Statement visitReassignment(BasicParser.ReassignmentContext ctx) {
    AssignLHS lhs = (AssignLHS) this.visit(ctx.assignLHS());
    AssignRHS rhs = (AssignRHS) this.visit(ctx.assignRHS());

    return new Statement(Statement.StatType.REASSIGNMENT, lhs, rhs);
  }

  @Override
  public Statement visitRead(BasicParser.ReadContext ctx) {
    AssignLHS lhs = (AssignLHS) this.visit(ctx.assignLHS());

    return new Statement(Statement.StatType.READ, lhs);
  }

  @Override
  public Statement visitFree(BasicParser.FreeContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new Statement(Statement.StatType.FREE, expression);
  }

  @Override
  public Statement visitReturn(BasicParser.ReturnContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new Statement(Statement.StatType.RETURN, expression);
  }

  @Override
  public Statement visitExit(BasicParser.ExitContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new Statement(Statement.StatType.EXIT, expression);
  }

  @Override
  public Statement visitPrint(BasicParser.PrintContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new Statement(Statement.StatType.PRINT, expression);
  }

  @Override
  public Statement visitPrintln(BasicParser.PrintlnContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new Statement(Statement.StatType.PRINTLN, expression);
  }

  @Override
  public Statement visitIf_then_else_fi(BasicParser.If_then_else_fiContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());
    Statement sThen = (Statement) this.visit(ctx.stat(0));
    Statement sElse = (Statement) this.visit(ctx.stat(1));

    return new Statement(Statement.StatType.IF, expression, sThen, sElse);
  }

  @Override
  public Statement visitWhile_do_done(BasicParser.While_do_doneContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());
    Statement statement = (Statement) this.visit(ctx.stat());

    return new Statement(Statement.StatType.WHILE, expression, statement);
  }

  @Override
  public Statement visitBegin_end(BasicParser.Begin_endContext ctx) {
    Statement statement = (Statement) this.visit(ctx.stat());

    return new Statement(Statement.StatType.BEGIN, statement);
  }

  @Override
  public Statement visitSemi_colon(BasicParser.Semi_colonContext ctx) {
    Statement statement1 = (Statement) visit(ctx.stat(0));
    Statement statement2 = (Statement) visit(ctx.stat(1));

    return new Statement(Statement.StatType.CONCAT, statement1, statement2);
  }

  @Override
  public List<Param> visitParamList(BasicParser.ParamListContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<Param> params = new ArrayList<>();

    for (int i = 0; i < ctx.param().size(); i++) {
      params.add((Param) this.visit(ctx.param(i)));
    }

    return params;
  }

  @Override
  public Param visitParam(BasicParser.ParamContext ctx) {
    Type type = (Type) this.visit(ctx.type());
    String ident = (String) this.visit(ctx.IDENT());
    return new Param(type, ident);
  }

  @Override
  public Type visitIntType(BasicParser.IntTypeContext ctx) {
    return new Type(Type.EType.INT);
  }

  @Override
  public Type visitBoolType(BasicParser.BoolTypeContext ctx) {
    return new Type(Type.EType.BOOL);
  }

  @Override
  public Type visitCharType(BasicParser.CharTypeContext ctx) {
    return new Type(Type.EType.CHAR);
  }

  @Override
  public Type visitStringType(BasicParser.StringTypeContext ctx) {
    return new Type(Type.EType.STRING);
  }

  @Override
  public Type visitBaseArrayType(BasicParser.BaseArrayTypeContext ctx) {
    Type arrayType = (Type) this.visit(ctx.baseType());

    return new Type(Type.EType.ARRAY, arrayType);
  }

  @Override public Type visitNestedArrayType(BasicParser.NestedArrayTypeContext ctx) {
    Type arrayType = (Type) this.visit(ctx.arrayType());

    return new Type(Type.EType.ARRAY, arrayType);
  }

  @Override public Type visitPairArrayType(BasicParser.PairArrayTypeContext ctx) {
    Type arrayType = (Type) this.visit(ctx.pairType());

    return new Type(Type.EType.ARRAY, arrayType);
  }

}
