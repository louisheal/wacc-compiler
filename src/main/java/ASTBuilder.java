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
    List<Param> params = null;
    Statement statement = (Statement) this.visit(ctx.stat());

    return new Function(returnType, ident, params, statement);
  }

}
