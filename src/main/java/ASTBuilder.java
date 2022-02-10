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

    BasicParser.FuncContext f = ctx.func(0);

    for (int i = 1; f != null; i++) {
      functions.add(visitFunc(f));
      f = ctx.func(i);
    }

    return null;
  }

  @Override
  public Function visitFunc(BasicParser.FuncContext ctx) {
    return null;
  }

}
