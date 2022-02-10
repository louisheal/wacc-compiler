import antlr.BasicParser;
import antlr.BasicParserBaseVisitor;
import ast.Function;
import ast.Program;
import ast.Statement;

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
    return null;
  }
}
