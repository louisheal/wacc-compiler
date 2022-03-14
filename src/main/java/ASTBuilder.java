import static java.lang.System.exit;


import antlr.BasicParser;
import antlr.BasicParserBaseVisitor;
import ast.*;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends BasicParserBaseVisitor<Object> {

  //PROGRAM

  @Override
  public Program visitProg(BasicParser.ProgContext ctx) {

    List<Function> functions = new ArrayList<>();
    Statement statement = (Statement) this.visit(ctx.stat());

    for (int i = 0; i < ctx.func().size(); i++) {
      functions.add((Function) this.visit(ctx.func(i)));
    }

    return new Program(functions, statement);
  }

  //FUNCTION

  @Override
  public Function visitFunc(BasicParser.FuncContext ctx) {
    Type returnType = (Type) this.visit(ctx.type());
    String ident = ctx.IDENT().getText();
    List<Param> params = visitParamList(ctx.paramList());
    Statement statement = (Statement) this.visit(ctx.stat());

    return new Function(returnType, ident, params, statement);
  }

  //STATEMENTS

  @Override
  public Statement visitSkip(BasicParser.SkipContext ctx) {
    return new StatementBuilder().buildSkip();
  }

  @Override
  public Statement visitDeclaration(BasicParser.DeclarationContext ctx) {
    Type type = (Type) this.visit(ctx.type());
    String ident = ctx.IDENT().getText();
    AssignRHS rhs = (AssignRHS) this.visit(ctx.assignRHS());

    return new StatementBuilder().buildDeclaration(type, ident, rhs);
  }

  @Override
  public Statement visitReassignment(BasicParser.ReassignmentContext ctx) {
    AssignLHS lhs = (AssignLHS) this.visit(ctx.assignLHS());
    AssignRHS rhs = (AssignRHS) this.visit(ctx.assignRHS());

    return new StatementBuilder().buildReassignment(lhs, rhs);
  }

  @Override
  public Statement visitRead(BasicParser.ReadContext ctx) {
    AssignLHS lhs = (AssignLHS) this.visit(ctx.assignLHS());

    return new StatementBuilder().buildRead(lhs);
  }

  @Override
  public Statement visitFree(BasicParser.FreeContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new StatementBuilder().buildFree(expression);
  }

  @Override
  public Statement visitReturn(BasicParser.ReturnContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new StatementBuilder().buildReturn(expression);
  }

  @Override
  public Statement visitExit(BasicParser.ExitContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new StatementBuilder().buildExit(expression);
  }

  @Override
  public Statement visitPrint(BasicParser.PrintContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new StatementBuilder().buildPrint(expression);
  }

  @Override
  public Statement visitPrintln(BasicParser.PrintlnContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());

    return new StatementBuilder().buildPrintln(expression);
  }

  @Override
  public Statement visitIf_then_else_fi(BasicParser.If_then_else_fiContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());
    Statement sThen = (Statement) this.visit(ctx.stat(0));
    Statement sElse = (Statement) this.visit(ctx.stat(1));

    return new StatementBuilder().buildIfThenElse(expression, sThen, sElse);
  }

  @Override
  public Statement visitWhile_do_done(BasicParser.While_do_doneContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());
    Statement statement = (Statement) this.visit(ctx.stat());

    return new StatementBuilder().buildWhile(expression, statement);
  }

  @Override
  public Statement visitBegin_end(BasicParser.Begin_endContext ctx) {
    Statement statement = (Statement) this.visit(ctx.stat());

    return new StatementBuilder().buildBegin(statement);
  }

  @Override
  public Statement visitSemi_colon(BasicParser.Semi_colonContext ctx) {
    Statement statement1 = (Statement) visit(ctx.stat(0));
    Statement statement2 = (Statement) visit(ctx.stat(1));

    return new StatementBuilder().buildSemiColon(statement1, statement2);
  }

  //PARAMS

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
    String ident = ctx.IDENT().getText();
    return new Param(type, ident);
  }

  //TYPES

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

  //ASSIGN-LHS

  @Override
  public AssignLHS visitIdentLHS(BasicParser.IdentLHSContext ctx) {
    return new AssignLHSBuilder().buildIdentLHS(ctx.IDENT().getText());
  }

  @Override
  public AssignLHS visitArrayLHS(BasicParser.ArrayLHSContext ctx) {
    return new AssignLHSBuilder().buildArrayLHS((ArrayElem) this.visit(ctx.arrayElem()));
  }

  @Override
  public AssignLHS visitPairLHS(BasicParser.PairLHSContext ctx) {
    return new AssignLHSBuilder().buildPairLHS((PairElem) this.visit(ctx.pairElem()));
  }

  //ARRAY-ELEM

  @Override
  public ArrayElem visitArrayElem(BasicParser.ArrayElemContext ctx) {
    List<Expression> expressions = new ArrayList<>();

    for (int i = 0; i < ctx.expr().size(); i++) {
      expressions.add((Expression) this.visit(ctx.expr(i)));
    }

    return new ArrayElem(ctx.IDENT().getText(), expressions);
  }

  //PAIR-ELEM

  @Override
  public PairElem visitFstElem(BasicParser.FstElemContext ctx) {
    return new PairElem(PairElem.PairElemType.FST, (Expression) this.visit(ctx.expr()));
  }

  @Override
  public PairElem visitSndElem(BasicParser.SndElemContext ctx) {
    return new PairElem(PairElem.PairElemType.SND, (Expression) this.visit(ctx.expr()));
  }

  //ASSIGN-RHS

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
    StringBuilder functionIdent = new StringBuilder();
    functionIdent.append(ctx.IDENT().getText()).append("_");

    for (int i = 0; i < ctx.argList().expr().size(); i++) {
      expressions.add((Expression) this.visit(ctx.argList().expr(i)));
      SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
      //Appends types onto the function name to match with Function class
      functionIdent.append(semanticAnalysis.getExpressionType((Expression) this.visit(ctx.argList().expr(i)))).append("_");
    }

    return new AssignRHSBuilder().buildCallRHS(functionIdent.substring(0, functionIdent.lastIndexOf("_")), expressions);
  }

  //EXPRESSIONS

  //INT-EXPR
  @Override
  public Expression visitIntExpr(BasicParser.IntExprContext ctx) {
    return new ExpressionBuilder().buildIntExpr(visitIntLiter(ctx.intLiter()));
  }

  @Override
  public Long visitIntLiter(BasicParser.IntLiterContext ctx) {
    long max = (long) (Math.pow(2, 31) - 1);
    long min = (long) -Math.pow(2, 31);
    long value = Long.parseLong(ctx.INTEGER().getText());
    if (value > max | value < min) {
      System.out.println("#syntax_error: Number out of bounds#");
      exit(100);
    }
    return Long.parseLong(ctx.INTEGER().getText());
  }

  //SIGNED-INT-EXPR
  @Override
  public Expression visitSignedIntExpr(BasicParser.SignedIntExprContext ctx) {
    return new ExpressionBuilder().buildIntExpr((Long) this.visit(ctx.signedIntLiter()));
  }

  @Override
  public Long visitPositiveInt(BasicParser.PositiveIntContext ctx) {
    return Long.parseLong(ctx.INTEGER().getText());
  }

  @Override
  public Long visitNegativeInt(BasicParser.NegativeIntContext ctx) {
    return (-1) * Long.parseLong(ctx.INTEGER().getText());
  }

  //BOOL-EXPR
  @Override
  public Expression visitBoolExpr(BasicParser.BoolExprContext ctx) {
    return new ExpressionBuilder().buildBoolExpr(visitBoolLiter(ctx.boolLiter()));
  }

  @Override
  public Boolean visitBoolLiter(BasicParser.BoolLiterContext ctx) {
    return Boolean.parseBoolean(ctx.BOOL_LITER().getText());
  }

  //CHAR-EXPR
  @Override
  public Expression visitCharExpr(BasicParser.CharExprContext ctx) {
    return new ExpressionBuilder().buildCharExpr(visitCharLiter(ctx.charLiter()));
  }

  @Override
  public Character visitCharLiter(BasicParser.CharLiterContext ctx) {
    if (ctx.CHAR_LITER().getText().charAt(1) == '\\') {
      switch (ctx.CHAR_LITER().getText().charAt(2)) {
        case '0':
          return '\0';
        case 'b':
          return '\b';
        case 't':
          return '\t';
        case 'n':
          return '\n';
        case 'f':
          return '\f';
        case 'r':
          return '\r';
        case '"':
          return '\"';
        case '\'':
          return '\'';
        case '\\':
          return '\\';
      }
    }
    return ctx.CHAR_LITER().getText().charAt(1);
  }

  //STRING-EXPR
  @Override
  public Expression visitStringExpr(BasicParser.StringExprContext ctx) {
    return new ExpressionBuilder().buildStringExpr(visitStringLiter(ctx.stringLiter()));
  }

  @Override
  public String visitStringLiter(BasicParser.StringLiterContext ctx) {
    return ctx.STR_LITER().getText();
  }

  //IDENT-EXPR
  @Override
  public Expression visitIdentExpr(BasicParser.IdentExprContext ctx) {
    return new ExpressionBuilder().buildIdentExpr(ctx.IDENT().getText());
  }

  //ARRAY-EXPR
  @Override
  public Expression visitArrayExpr(BasicParser.ArrayExprContext ctx) {
    return new ExpressionBuilder().buildArrayExpr(visitArrayElem(ctx.arrayElem()));
  }

  //UNARY-OP
  @Override
  public Expression visitUnOp(BasicParser.UnOpContext ctx) {

    Expression.ExprType exprType = (Expression.ExprType) this.visit(ctx.unaryOper());
    Expression expression = (Expression) this.visit(ctx.expr());

    return new ExpressionBuilder().buildUnOpExpr(exprType, expression);
  }

  //NOT
  @Override
  public Expression.ExprType visitNot(BasicParser.NotContext ctx) {
    return Expression.ExprType.NOT;
  }

  //NEGATE
  @Override
  public Expression.ExprType visitNeg(BasicParser.NegContext ctx) {
    return Expression.ExprType.NEG;
  }

  //LENGTH
  @Override
  public Expression.ExprType visitLen(BasicParser.LenContext ctx) {
    return Expression.ExprType.LEN;
  }

  //ORD
  @Override
  public Expression.ExprType visitOrd(BasicParser.OrdContext ctx) {
    return Expression.ExprType.ORD;
  }

  //CHR
  @Override
  public Expression.ExprType visitChr(BasicParser.ChrContext ctx) {
    return Expression.ExprType.CHR;
  }

  //BINARY-OP

  //DIVIDE
  @Override
  public Expression visitDivExpr(BasicParser.DivExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildDivExpr(expression1, expression2);
  }

  //MULTIPLY
  @Override
  public Expression visitMulExpr(BasicParser.MulExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildMulExpr(expression1, expression2);
  }

  //MODULO
  @Override
  public Expression visitModExpr(BasicParser.ModExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildModExpr(expression1, expression2);
  }

  //PLUS
  @Override
  public Expression visitPlusExpr(BasicParser.PlusExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildPlusExpr(expression1, expression2);
  }

  //MINUS
  @Override
  public Expression visitMinusExpr(BasicParser.MinusExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildMinusExpr(expression1, expression2);
  }

  //GREATER-THAN
  @Override
  public Expression visitGtExpr(BasicParser.GtExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildGtExpr(expression1, expression2);
  }

  //GREATER-THAN-OR-EQUAL
  @Override
  public Expression visitGteExpr(BasicParser.GteExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildGteExpr(expression1, expression2);
  }

  //LESS-THAN
  @Override
  public Expression visitLtExpr(BasicParser.LtExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildLtExpr(expression1, expression2);
  }

  //LESS-THAN-OR-EQUAL
  @Override
  public Expression visitLteExpr(BasicParser.LteExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildLteExpr(expression1, expression2);
  }

  //EQUAL
  @Override
  public Expression visitEqExpr(BasicParser.EqExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildEqExpr(expression1, expression2);
  }

  //NOT-EQUAL
  @Override
  public Expression visitNeqExpr(BasicParser.NeqExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildNeqExpr(expression1, expression2);
  }

  //AND
  @Override
  public Expression visitAndExpr(BasicParser.AndExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildAndExpr(expression1, expression2);
  }

  //OR
  @Override
  public Expression visitOrExpr(BasicParser.OrExprContext ctx) {

    Expression expression1 = (Expression) this.visit(ctx.expr(0));
    Expression expression2 = (Expression) this.visit(ctx.expr(1));

    return new ExpressionBuilder().buildOrExpr(expression1, expression2);
  }

  //BRACKETS
  @Override
  public Expression visitBrExpr(BasicParser.BrExprContext ctx) {
    Expression expression = (Expression) this.visit(ctx.expr());
    return new ExpressionBuilder().buildBracketsExpr(expression);
  }

}
