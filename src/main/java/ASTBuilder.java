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

}
