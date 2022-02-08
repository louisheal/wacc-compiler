import static java.lang.System.exit;

import antlr.*;

class MyVisitor extends BasicParserBaseVisitor<Object> {
    String semanticError = "#semantic_error#";
    String syntaxError = "#syntax_error";

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

    @Override
    public Object visitDeclaration(BasicParser.DeclarationContext ctx) {

        try {
            if (ctx.type().baseType().INT().toString().equals("int")) { // LHS type is int
//                System.out.println("Declaration type: int");
                try {
                    ctx.assignRHS().expr(0).intLiter().INTEGER();
                } catch (NullPointerException ignored) {
                    // RHS is not int
                    System.out.println(semanticError);
                    exit(200);
                }
            }
        } catch (NullPointerException ignored) {
            try {
                if (ctx.type().baseType().BOOL().toString().equals("bool")) { // LHS type is bool
//                    System.out.println("Declaration type: bool");
                    try {
                        ctx.assignRHS().expr(0).boolLiter().TRUE();
                    } catch (NullPointerException ignored2) {
                        // RHS is not bool
                        System.out.println(semanticError);
                        exit(200);
                    }
                }
            } catch (NullPointerException ignored2){
                try {
                    if (ctx.type().baseType().CHAR().toString().equals("char")) { // LHS type is char
//                        System.out.println("Declaration type: char");
                        try {
                            ctx.assignRHS().expr(0).charLiter().CHAR_LITER();
                        } catch (NullPointerException ignored3) {
                            // RHS is not a char
                            System.out.println(semanticError);
                            exit(200);
                        }
                    }
                } catch (NullPointerException ignored3) {
                    try {
                        if (ctx.type().baseType().STRING().toString().equals("string")) { // LHS type is string
//                            System.out.println("Declaration type: string");
                            try {
                                ctx.assignRHS().expr(0).stringLiter().STR_LITER();
                            } catch (NullPointerException ignored4) {
                                // RHS is not a string
                                System.out.println(semanticError);
                                exit(200);
                            }
                        }
                    } catch (NullPointerException ignored4) {
                        System.out.println("Type is not: int, bool, char, or string");
                    }
                }
            }
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
}
