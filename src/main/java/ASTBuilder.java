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
    List<Param> params = new ArrayList<>();

    if (ctx == null) {
      return params;
    }

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
    return new Type(Type.EType.ARRAY, (Type) this.visit(ctx.baseType()));
  }

  @Override public Type visitNestedArrayType(BasicParser.NestedArrayTypeContext ctx) {
    return new Type(Type.EType.ARRAY, (Type) this.visit(ctx.arrayType()));
  }

  @Override public Type visitPairArrayType(BasicParser.PairArrayTypeContext ctx) {
    return new Type(Type.EType.ARRAY, (Type) this.visit(ctx.pairType()));
  }

  @Override
  public Type visitPairType(BasicParser.PairTypeContext ctx) {
    Type fstType = (Type) this.visit(ctx.pairElemType(0));
    Type sndType = (Type) this.visit(ctx.pairElemType(1));

    return new Type(Type.EType.PAIR, fstType, sndType);
  }

  @Override
  public Type visitBasePairElem(BasicParser.BasePairElemContext ctx) {
    return (Type) this.visit(ctx.baseType());
  }

  @Override
  public Type visitArrayPairElem(BasicParser.ArrayPairElemContext ctx) {
    return (Type) this.visit(ctx.arrayType());
  }

  @Override
  public Type visitPairPairElem(BasicParser.PairPairElemContext ctx) {
    return new Type(Type.EType.PAIR);
  }

  @Override
  public AssignLHS visitIdentLHS(BasicParser.IdentLHSContext ctx) {
    return new AssignLHS(AssignLHS.LHSType.IDENT, ctx.IDENT().getText());
  }

  @Override
  public AssignLHS visitArrayLHS(BasicParser.ArrayLHSContext ctx) {
    return new AssignLHS(AssignLHS.LHSType.ARRAYELEM, (ArrayElem) this.visit(ctx.arrayElem()));
  }

  @Override
  public AssignLHS visitPairLHS(BasicParser.PairLHSContext ctx) {
    return new AssignLHS(AssignLHS.LHSType.PAIRELEM, (PairElem) this.visit(ctx.pairElem()));
  }

  @Override
  public ArrayElem visitArrayElem(BasicParser.ArrayElemContext ctx) {
    List<Expression> expressions = new ArrayList<>();

    for (int i = 0; i < ctx.expr().size(); i++) {
      expressions.add((Expression) this.visit(ctx.expr(i)));
    }

    return new ArrayElem(ctx.IDENT().getText(), expressions);
  }

  @Override
  public PairElem visitFstElem(BasicParser.FstElemContext ctx) {
    return new PairElem(PairElem.PairElemType.FST, (Expression) this.visit(ctx.expr()));
  }

  @Override
  public PairElem visitSndElem(BasicParser.SndElemContext ctx) {
    return new PairElem(PairElem.PairElemType.SND, (Expression) this.visit(ctx.expr()));
  }

  @Override
  public AssignRHS visitExprRHS(BasicParser.ExprRHSContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new AssignRHSBuilder().buildExprRHS(expression);
  }

  @Override
  public AssignRHS visitArrayRHS(BasicParser.ArrayRHSContext ctx) {
    List<Expression> expressions = new ArrayList<>();

    for (int i = 0; i < ctx.arrayLiter().expr().size(); i++) {
      expressions.add((Expression) this.visit(ctx.arrayLiter().expr(i)));
    }

    return new AssignRHSBuilder().buildArrayRHS(expressions);
  }

  @Override
  public AssignRHS visitNewPairRHS(BasicParser.NewPairRHSContext ctx) {
    Expression fst = (Expression) this.visit(ctx.expr(0));
    Expression snd = (Expression) this.visit(ctx.expr(1));

    return new AssignRHSBuilder().buildNewPair(fst, snd);
  }

  @Override
  public AssignRHS visitPairElemRHS(BasicParser.PairElemRHSContext ctx) {
    PairElem pairElem = (PairElem) this.visit(ctx.pairElem());

    return new AssignRHSBuilder().buildPairElem(pairElem);
  }

  @Override
  public AssignRHS visitCallRHS(BasicParser.CallRHSContext ctx) {

    List<Expression> expressions = new ArrayList<>();
    String functionIdent = ctx.IDENT().getText();

    for (int i = 0; i < ctx.argList().expr().size(); i++) {
      expressions.add((Expression) this.visit(ctx.argList().expr(i)));
    }

    return new AssignRHSBuilder().buildCallRHS(functionIdent, expressions);
  }

  @Override
  public Expression visitIntExpr(BasicParser.IntExprContext ctx) {
    return new Expression(Expression.ExprType.INTLITER, (Integer) this.visit(ctx.intLiter()));
  }

  @Override
  public Integer visitIntLiter(BasicParser.IntLiterContext ctx) {
    return Integer.parseInt(ctx.INTEGER().getText());
  }

}
