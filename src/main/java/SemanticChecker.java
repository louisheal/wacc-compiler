import static java.lang.System.exit;

import antlr.*;
import antlr.BasicParser.ExprContext;
import antlr.BasicParser.PairElemContext;
import antlr.BasicParser.PairElemTypeContext;
import java.util.Arrays;
import org.antlr.v4.runtime.Token;

import java.awt.*;
import java.util.ArrayList;

class SemanticChecker extends BasicParserBaseVisitor<Object> {

    String syntaxError = "#syntax_error#";

    SymbolTable currentST = new SymbolTable(null);

    private int errors = 0;

    public int getNumberOfSemanticErrors() {
        return errors;
    }

    private void printSemanticError(Error error, Type lType, Type rType, Token token) {

        String errorMsg = "Semantic Error at " + token.getLine() + ":" + token.getCharPositionInLine() + " -- ";

        switch (error) {
            case IncompatibleTypes: errorMsg += "Incompatible type at " + token.getText() +
                                                " (expected: " + lType + ", actual: " + rType + ")";
            case NotDefined: errorMsg += "Variable " + token.getText() + " is not defined in this scope";
                                                break;
        }

        System.out.println(errorMsg);

    }

    @Override public Object visitProg(BasicParser.ProgContext ctx) { return visitChildren(ctx); }

    @Override public Object visitFunc(BasicParser.FuncContext ctx) { return visitChildren(ctx); }

    @Override public Object visitParamList(BasicParser.ParamListContext ctx) { return visitChildren(ctx); }

    @Override public Object visitParam(BasicParser.ParamContext ctx) { return visitChildren(ctx); }

    @Override public Object visitIntLiter(BasicParser.IntLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitSignedIntLiter(BasicParser.SignedIntLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitBoolLiter(BasicParser.BoolLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitCharLiter(BasicParser.CharLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitStringLiter(BasicParser.StringLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitReassignment(BasicParser.ReassignmentContext ctx) {
      Type rhsType = getRHSType(ctx.assignRHS());
//      if (ctx.assignLHS().getText().contains("[")) {
//        String arrayType = ctx.assignLHS().getText().replaceAll("/^[-_a-zA-Z0-9.]+$/", "");
//        if (arrayType.equals("String")) {
//          printSemanticError(Error.IncompatibleTypes, Type.ARRAY, rhsType, ctx.assignLHS().start);
//          return visitChildren(ctx);
//        }
//      }

      String varName = ctx.assignLHS().IDENT().getText();
      if (currentST.contains(varName) & (rhsType != currentST.getType(varName))) {
        printSemanticError(Error.IncompatibleTypes, currentST.getType(varName), rhsType, ctx.stop);
      }

      return visitChildren(ctx);
    }

    @Override public Object visitSemi_colon(BasicParser.Semi_colonContext ctx) { return visitChildren(ctx); }

    @Override public Object visitRead(BasicParser.ReadContext ctx) { return visitChildren(ctx); }

    @Override public Object visitWhile_do_done(BasicParser.While_do_doneContext ctx) {
      String expr = ctx.expr().getText();
      if (!expr.equals("true") & !expr.equals("false")) {
        if (currentST.contains(expr)) {
          if (currentST.getType(expr) != Type.BOOL) {
            errors++;
            printSemanticError(Error.IncompatibleTypes, Type.BOOL, currentST.getType(expr), ctx.start);
          }
        } else {
          errors++;
          printSemanticError(Error.NotDefined, null, null, ctx.expr().start);
        }
      }
      return visitChildren(ctx);
    }

    @Override public Object visitSkip(BasicParser.SkipContext ctx) { return visitChildren(ctx); }

    private Type getExpressionContextType(BasicParser.ExprContext expr) {
        if (expr.intLiter() != null) {
            return Type.INT;
        }
        if (expr.boolLiter() != null) {
            return Type.BOOL;
        }
        if (expr.charLiter() != null) {
            return Type.CHAR;
        }
        if (expr.stringLiter() != null) {
            return Type.STRING;
        }
        return Type.OTHER;
    }

    private Type getTypeContextType(BasicParser.TypeContext type) {
        if (type.baseType() != null) {
            if (type.baseType().INT() != null) {
                return Type.INT;
            }
            if (type.baseType().BOOL() != null) {
                return Type.BOOL;
            }
            if (type.baseType().CHAR() != null) {
                return Type.CHAR;
            }
            if (type.baseType().STRING() != null) {
                return Type.STRING;
            }
        }
        if (type.pairType() != null) {
            return Type.PAIR;
        }
        if (type.arrayType() != null) {
            return Type.ARRAY;
        }
        return Type.OTHER;
    }

    private boolean checkAllBoolValues(String[] values) {

      for (String value : values) {
        if (!(value.equals("true") | value.equals("false"))) {
          if (!currentST.contains(value)) {
            return false;
          } else {
            if (currentST.getType(value) != Type.BOOL) {
              return false;
            }
          }
        }
      }
      return true;
    }

    public boolean checkIfIsInt(String[] values) {
      for (String value : values) {
        try {
          Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
          if (!currentST.contains(value)) {
            return false;
          } else {
            if (currentST.getType(value) != Type.INT) {
              return false;
            }
          }
        }
      }
      return true;
    }

    private Type parseRHS(String expr) {

      boolean isBoolean = expr.contains("||") | expr.contains("&&") | expr.contains("==") | expr.contains("!=");
      boolean isIntBoolean = expr.contains(">") | expr.contains(">=") | expr.contains("<") | expr.contains("<=");
      boolean isInt = expr.contains("+") | expr.contains("-") | expr.contains("*") | expr.contains("/") |
          expr.contains("%");
      boolean errorCheck = (isBoolean & isIntBoolean) | (isBoolean & isInt) | (isIntBoolean & isInt);

      if (errorCheck) {
        return null;
      } else if (isBoolean) {
        String[] values = expr.split("\\|\\||&&|==|!=");
        if (!checkAllBoolValues(values)) {
          return Type.ERROR;
        }
        return Type.BOOL;
      } else if (isIntBoolean) {
        String[] values = expr.split("<|>|<=|>=");
        if (!checkIfIsInt(values)) {
          return Type.ERROR;
        }
        return Type.BOOL;
      } else if (isInt) {
        String[] values = expr.split("/+|-|/*|/");
        if (!checkIfIsInt(values)) {
          return Type.ERROR;
        }
        return Type.INT;
      }
      return Type.OTHER;
    }

    private Type getRHSType(BasicParser.AssignRHSContext ctx) {
        String rhs = ctx.getText();

        if (ctx.pairElem() != null) {
            return Type.OTHER;
        }

        if (ctx.NEW_PAIR() != null) {
          return Type.PAIR;
        }

        if (ctx.arrayLiter() != null) {
            return Type.ARRAY;
        }

        if (ctx.expr(0) == null) {
          return Type.OTHER;
        }

        if (ctx.expr(0).intLiter() != null) {
            return Type.INT;
        }
        if (ctx.expr(0).boolLiter() != null) {
            return Type.BOOL;
        }
        if (ctx.expr(0).charLiter() != null) {
            return Type.CHAR;
        }
        if (ctx.expr(0).stringLiter() != null) {
            return Type.STRING;
        }
        if (ctx.expr(0).pairLiter() != null) {
            return Type.PAIR;
        }

        if (parseRHS(rhs) == null) {
          return Type.ERROR;
        }

        return Type.OTHER;
    }

    private Token getErrorPos(Type type, BasicParser.AssignRHSContext ctx) {
        switch (type) {
            case INT:    return ctx.expr(0).intLiter().INTEGER().getSymbol();
            case BOOL:   return ctx.expr(0).boolLiter().BOOL_LITER().getSymbol();
            case CHAR:   return ctx.expr(0).charLiter().CHAR_LITER().getSymbol();
            case STRING: return ctx.expr(0).stringLiter().STR_LITER().getSymbol();
            case PAIR:   return ctx.expr(0).pairLiter().NULL().getSymbol();
        }
        return null;
    }

    private Type getArrayType(BasicParser.ArrayTypeContext ctx) {
        int typeNum = ctx.getStart().getType();
        return Type.values()[typeNum - 25]; //TODO: Change magic number
    }

  private Type getPairElemType(BasicParser.PairElemTypeContext ctx) {
    if (ctx.baseType() != null) {
      if (ctx.baseType().INT() != null) {
        return Type.INT;
      }
      if (ctx.baseType().BOOL() != null) {
        return Type.BOOL;
      }
      if (ctx.baseType().CHAR() != null) {
        return Type.CHAR;
      }
      if (ctx.baseType().STRING() != null) {
        return Type.STRING;
      }
    }
    if (ctx.arrayType() != null) {

    }
    if (ctx.PAIR() != null){
      return Type.PAIR;
    }
    return Type.OTHER;
  }


    private boolean matchingTypes(Type type, BasicParser.ExprContext expr) {
        switch (type) {
            case PAIR:   return expr.pairLiter() != null;
            case INT:    return expr.intLiter() != null;
            case BOOL:   return expr.boolLiter() != null;
            case CHAR:   return expr.charLiter() != null;
            case STRING: return expr.stringLiter() != null;
        }
        return false;
    }

  public Type getBaseTypeOfArray(String input) {
    input = input.substring(1, input.length() - 1);
    StringBuilder extract = new StringBuilder();

    for (char s : input.toCharArray()) {
      if (s != ',') {
        extract.append(s);
      } else {
        break;
      }
    }

    try {
      Integer.parseInt(extract.toString());
      return Type.INT;
    } catch (NumberFormatException e) {
      if (Boolean.parseBoolean(extract.toString())) {
        return Type.BOOL;
      } else if (extract.toString().charAt(0) == '\'') {
        return Type.CHAR;
      } else if (extract.toString().charAt(0) == '\"') {
        return Type.STRING;
      } else if (extract.toString().charAt(0) == '[') {
        return Type.ARRAY;
      }
    }

    return Type.OTHER;
  }

    private void validateArrayType(Type lType, BasicParser.ArrayLiterContext rType) {
        ArrayList<BasicParser.ExprContext> exprs = new ArrayList<>();
        BasicParser.ExprContext e = rType.expr(0);

        for (int i = 0; e != null; i++) {
            exprs.add(e);
            e = rType.expr(i + 1);
        }

      for (BasicParser.ExprContext expr : exprs) {
        String ident = expr.getText();
        if (!matchingTypes(lType, expr)) {
          String array = currentST.getValue(ident).getText();
          if (!(currentST.contains(ident) & ((currentST.getType(ident).equals(lType)) |
              getBaseTypeOfArray(array).equals(lType)))) {
            if (!(currentST.contains(ident) & (currentST.getType(ident).equals(lType)))) {
              errors++;
              printSemanticError(Error.IncompatibleTypes, lType,
                  getExpressionContextType(expr), expr.start);
              printSemanticError(Error.IncompatibleTypes, lType, getExpressionContextType(expr),
                  expr.start);
              break;
            }
          }
        }
      }
    }
    
    @Override
    public Object visitDeclaration(BasicParser.DeclarationContext ctx) {

        Type lhsType = getTypeContextType(ctx.type());
        Type rhsType = getRHSType(ctx.assignRHS());
        String varName = ctx.IDENT().getText();

        if (ctx.assignRHS().getText().equals("null")) {
          currentST.newSymbol(varName, rhsType, ctx.assignRHS());
          return visitChildren(ctx);
        }

        if (rhsType.equals(Type.ERROR)) {
          printSemanticError(Error.IncompatibleTypes, lhsType, rhsType, ctx.assignRHS().expr(0).start);
          return visitChildren(ctx);
        }

        if (lhsType == Type.OTHER || rhsType == Type.OTHER) {
            return visitChildren(ctx);
        }

        if (lhsType == Type.PAIR && rhsType == Type.PAIR) {
          Type lhsPairFst = getPairElemType(ctx.type().pairType().pairElemType(0));
          Type lhsPairSnd = getPairElemType(ctx.type().pairType().pairElemType(1));
          //if (ctx.assignRHS().pairElem() == null){
          //  return visitChildren(ctx); //short circuits and returns if the pair is null
          // } short circuiting code doesnt work
          if(!matchingTypes(lhsPairFst,ctx.assignRHS().expr(0))){
            errors++;
            printSemanticError(Error.IncompatibleTypes, lhsPairFst, rhsType,
                ctx.assignRHS().pairElem().expr().start);
          }
          if(!matchingTypes(lhsPairSnd,ctx.assignRHS().expr(1))){
            errors++;
            printSemanticError(Error.IncompatibleTypes, lhsPairSnd, rhsType,
                ctx.assignRHS().pairElem().expr().start);
          }
        }


        if (lhsType == Type.ARRAY && rhsType == Type.ARRAY) {
            validateArrayType(getArrayType(ctx.type().arrayType()), ctx.assignRHS().arrayLiter());
        }

        if (lhsType != rhsType) {
            errors += 1;
            Token rhsToken = getErrorPos(rhsType, ctx.assignRHS());
            printSemanticError(Error.IncompatibleTypes, lhsType, rhsType, rhsToken);
        }
        currentST.newSymbol(varName, rhsType, ctx.assignRHS());

        return visitChildren(ctx);
    }

    @Override public Object visitIf_then_else_fi(BasicParser.If_then_else_fiContext ctx) { return visitChildren(ctx); }

    @Override public Object visitExit(BasicParser.ExitContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPrint(BasicParser.PrintContext ctx) { return visitChildren(ctx); }



    @Override public Object visitPrintln(BasicParser.PrintlnContext ctx) {
      if (parseRHS(ctx.expr().getText()) != (Type.ERROR)) {
        return visitChildren(ctx);
      } else {
        errors++;
        printSemanticError(Error.NotDefined, null, null, ctx.stop);
      }
      return visitChildren(ctx);
    }

    @Override public Object visitBegin_end(BasicParser.Begin_endContext ctx) { return visitChildren(ctx); }

    @Override public Object visitFree(BasicParser.FreeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitReturn(BasicParser.ReturnContext ctx) { return visitChildren(ctx); }

    @Override public Object visitAssignLHS(BasicParser.AssignLHSContext ctx) { return visitChildren(ctx); }

    @Override
    public Object visitAssignRHS(BasicParser.AssignRHSContext ctx) {
        if (!ctx.expr().isEmpty()) { // Checks if the RHS is an expr
            try {
                double x = Double.parseDouble(ctx.expr(0).intLiter().INTEGER().toString());
                double max = (Math.pow(2, 31) - 1);
                double min = (0 - Math.pow(2, 31));
                if (x >= max | x <= min) { // This checks if the int of an expression is in range
                    System.out.println(syntaxError);
                    exit(100);
                }
            } catch (NullPointerException ignored) {}
        }
        return visitChildren(ctx);
    }

    @Override public Object visitExpr(BasicParser.ExprContext ctx) { return visitChildren(ctx); }

    @Override public Object visitArgList(BasicParser.ArgListContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPairElem(BasicParser.PairElemContext ctx) { return visitChildren(ctx); }

    @Override public Object visitType(BasicParser.TypeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitBaseType(BasicParser.BaseTypeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitBaseArrayType(BasicParser.BaseArrayTypeContext ctx) { return visitChildren(ctx); }


    @Override public Object visitNestedArrayType(BasicParser.NestedArrayTypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Object visitPairArrayType(BasicParser.PairArrayTypeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPairType(BasicParser.PairTypeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPairElemType(BasicParser.PairElemTypeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitUnaryOper(BasicParser.UnaryOperContext ctx) { return visitChildren(ctx); }

    @Override public Object visitArrayElem(BasicParser.ArrayElemContext ctx) { return visitChildren(ctx); }

    @Override public Object visitArrayLiter(BasicParser.ArrayLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPairLiter(BasicParser.PairLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitComment(BasicParser.CommentContext ctx) { return visitChildren(ctx); }

    enum Type {
        INT,
        BOOL,
        CHAR,
        STRING,
        PAIR,
        ARRAY,
        ERROR,
        ERROR1,
        OTHER
    }

    enum Error {
        IncompatibleTypes,
        NotDefined
    }

}
