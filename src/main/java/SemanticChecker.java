import static java.lang.System.exit;

import antlr.*;
import org.antlr.v4.runtime.Token;

import java.awt.*;
import java.util.ArrayList;

class SemanticChecker extends BasicParserBaseVisitor<Object> {

    String semanticError = "#semantic_error#";
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
                                                break;
        }

        System.out.println(errorMsg);

    }

    @Override public Object visitProg(BasicParser.ProgContext ctx) { return visitChildren(ctx); }

    @Override public Object visitFunc(BasicParser.FuncContext ctx) { return visitChildren(ctx); }

    @Override public Object visitParamList(BasicParser.ParamListContext ctx) { return visitChildren(ctx); }

    @Override public Object visitParam(BasicParser.ParamContext ctx) { return visitChildren(ctx); }

    @Override public Object visitIntLiter(BasicParser.IntLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitBoolLiter(BasicParser.BoolLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitCharLiter(BasicParser.CharLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitStringLiter(BasicParser.StringLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitReassignment(BasicParser.ReassignmentContext ctx) { return visitChildren(ctx); }

    @Override public Object visitSemi_colon(BasicParser.Semi_colonContext ctx) { return visitChildren(ctx); }

    @Override public Object visitRead(BasicParser.ReadContext ctx) { return visitChildren(ctx); }

    @Override public Object visitWhile_do_done(BasicParser.While_do_doneContext ctx) { return visitChildren(ctx); }

    @Override public Object visitSkip(BasicParser.SkipContext ctx) { return visitChildren(ctx); }

    private Type getExpressionContextType(BasicParser.ExprContext expr) {
        return Type.OTHER;
    }

    private Type getTypeContextType(BasicParser.TypeContext type) {
        if (type.baseType() != null) {
            if (type.baseType() != null) {
                return Type.INT;
            }
            if (type.baseType() != null) {
                return Type.BOOL;
            }
            if (type.baseType() != null) {
                return Type.CHAR;
            }
            if (type.baseType() != null) {
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

    private Type getRHSType(BasicParser.AssignRHSContext ctx) {
        return Type.OTHER;
    }

    private Token getErrorPos(Type type, BasicParser.AssignRHSContext ctx) {
        return null;
    }

    private Type getArrayType(BasicParser.ArrayTypeContext ctx) {
        int typeNum = ctx.getStart().getType();
        return Type.values()[typeNum - 25]; //TODO: Change magic number
    }

    private boolean matchingTypes(Type type, BasicParser.ExprContext expr) {
        return false;
    }

    private void validateArrayType(Type lType, BasicParser.ArrayLiterContext rType) {
        ArrayList<BasicParser.ExprContext> exprs = new ArrayList<>();
        BasicParser.ExprContext e = rType.expr(0);

        for (int i = 0; e != null; i++) {
            exprs.add(e);
            e = rType.expr(i + 1);
        }

        for (BasicParser.ExprContext expr : exprs) {
            if (!matchingTypes(lType, expr)) {
                errors++;
                printSemanticError(Error.IncompatibleTypes, lType, getExpressionContextType(expr), expr.start);
                break;
            }
        }
    }

    @Override
    public Object visitDeclaration(BasicParser.DeclarationContext ctx) {

        Type lhsType = getTypeContextType(ctx.type());
        Type rhsType = getRHSType(ctx.assignRHS());
        String varName = ctx.IDENT().getText();

        if (lhsType == Type.OTHER || rhsType == Type.OTHER) {
            return visitChildren(ctx);
        }

        if (lhsType != rhsType) {
            errors += 1;
            Token rhsToken = getErrorPos(rhsType, ctx.assignRHS());
            printSemanticError(Error.IncompatibleTypes, lhsType, rhsType, rhsToken);
        }
        currentST.newSymbol(varName, rhsType);

        return visitChildren(ctx);
    }

    @Override public Object visitIf_then_else_fi(BasicParser.If_then_else_fiContext ctx) { return visitChildren(ctx); }

    @Override public Object visitExit(BasicParser.ExitContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPrint(BasicParser.PrintContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPrintln(BasicParser.PrintlnContext ctx) { return visitChildren(ctx); }

    @Override public Object visitBegin_end(BasicParser.Begin_endContext ctx) { return visitChildren(ctx); }

    @Override public Object visitFree(BasicParser.FreeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitReturn(BasicParser.ReturnContext ctx) { return visitChildren(ctx); }

    @Override public Object visitArgList(BasicParser.ArgListContext ctx) { return visitChildren(ctx); }

    @Override public Object visitType(BasicParser.TypeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitBaseArrayType(BasicParser.BaseArrayTypeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitNestedArrayType(BasicParser.NestedArrayTypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Object visitPairArrayType(BasicParser.PairArrayTypeContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPairType(BasicParser.PairTypeContext ctx) { return visitChildren(ctx); }

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
        OTHER
    }

    enum Error {
        IncompatibleTypes
    }

}
