import antlr.*;
import java.io.IOException;
import java.nio.file.Path;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import static java.lang.System.exit;

public class Compiler {

  public static CommonTokenStream tokenize(CharStream input) {
    BasicLexer lexer = new BasicLexer(input);
    return new CommonTokenStream(lexer);
  }

  public static String parse(CommonTokenStream tokens) {
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    return tree.toStringTree(parser);
  }
  
  public static String lexAnalyse(String program) {
    CharStream input = CharStreams.fromString(program);
    return parse(tokenize(input));
  }

  public static void main(String[] args) throws IOException {

    Path filename = Path.of(args[0]);

    CharStream input = CharStreams.fromPath(filename);

    BasicLexer lexer = new BasicLexer(input);

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    BasicParser parser = new BasicParser(tokens);

    ParseTree tree = parser.prog();

    if (parser.getNumberOfSyntaxErrors() > 0) {
      System.out.println("#syntax_error#");
      exit(100);
    }

    //System.out.println(tree.toStringTree(parser));

  }

}
