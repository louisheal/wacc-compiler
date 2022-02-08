import antlr.*;

class MyVisitor extends BasicParserBaseVisitor<Object> {
    @Override
    public Object visitComment(BasicParser.CommentContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitPairLiter(BasicParser.PairLiterContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitArrayLiter(BasicParser.ArrayLiterContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitArrayElem(BasicParser.ArrayElemContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitBinaryOper(BasicParser.BinaryOperContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitUnaryOper(BasicParser.UnaryOperContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitExpr(BasicParser.ExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitPairElemBaseType(BasicParser.PairElemBaseTypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitPairElemType(BasicParser.PairElemTypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitPairType(BasicParser.PairTypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitArrayType(BasicParser.ArrayTypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitBaseType(BasicParser.BaseTypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitType(BasicParser.TypeContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public Object visitPairElem(BasicParser.PairElemContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitArgList(BasicParser.ArgListContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitAssignRHS(BasicParser.AssignRHSContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitAssignLHS(BasicParser.AssignLHSContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitStat(BasicParser.StatContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitParam(BasicParser.ParamContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitParamList(BasicParser.ParamListContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitFunc(BasicParser.FuncContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitProg(BasicParser.ProgContext ctx) {
        return visitChildren(ctx);
    }
}
