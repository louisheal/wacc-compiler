import static java.lang.System.exit;

import antlr.*;
import org.antlr.v4.runtime.Token;

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

    @Override public Object visitSignedIntLiter(BasicParser.SignedIntLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitBoolLiter(BasicParser.BoolLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitCharLiter(BasicParser.CharLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitStringLiter(BasicParser.StringLiterContext ctx) { return visitChildren(ctx); }

    @Override public Object visitBinaryOper(BasicParser.BinaryOperContext ctx) { return visitChildren(ctx); }

    @Override public Object visitReassignment(BasicParser.ReassignmentContext ctx) { return visitChildren(ctx); }

    @Override public Object visitSemi_colon(BasicParser.Semi_colonContext ctx) { return visitChildren(ctx); }

    @Override public Object visitRead(BasicParser.ReadContext ctx) { return visitChildren(ctx); }

    @Override public Object visitWhile_do_done(BasicParser.While_do_doneContext ctx) { return visitChildren(ctx); }

    @Override public Object visitSkip(BasicParser.SkipContext ctx) { return visitChildren(ctx); }

    private Type getTypeContextType(BasicParser.TypeContext type) {
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
        return null;
    }

    private Type getRHSType(BasicParser.AssignRHSContext ctx) {
        if (ctx.expr(0).intLiter() != null) {
            return Type.INT;
        }
        else if (ctx.expr(0).boolLiter() != null) {
            return Type.BOOL;
        }
        else if (ctx.expr(0).charLiter() != null) {
            return Type.CHAR;
        }
        else {
            return Type.STRING;
        }
    }

    private Token getErrorPos(Type type, BasicParser.AssignRHSContext ctx) {
        switch (type) {
            case INT:    return ctx.expr(0).intLiter().INTEGER().getSymbol();
            case BOOL:   return ctx.expr(0).boolLiter().TRUE().getSymbol();
            case CHAR:   return ctx.expr(0).charLiter().CHAR_LITER().getSymbol();
            case STRING: return ctx.expr(0).stringLiter().STR_LITER().getSymbol();
        }
        return null;
    }

    @Override
    public Object visitDeclaration(BasicParser.DeclarationContext ctx) {

        Type lhsType = getTypeContextType(ctx.type());
        Type rhsType = getRHSType(ctx.assignRHS());
        Token rhsToken = getErrorPos(rhsType, ctx.assignRHS());

        if (lhsType != rhsType) {
            errors += 1;
            printSemanticError(Error.IncompatibleTypes, lhsType, rhsType, rhsToken);
        }

        return visitChildren(ctx);
    }

    @Override public Object visitIf_then_else_fi(BasicParser.If_then_else_fiContext ctx) { return visitChildren(ctx); }

    @Override public Object visitExit(BasicParser.ExitContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPrint(BasicParser.PrintContext ctx) { return visitChildren(ctx); }

    @Override public Object visitPrintln(BasicParser.PrintlnContext ctx) { return visitChildren(ctx); }

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
        ARRAY
    }

    enum Error {
        IncompatibleTypes
    }

}
