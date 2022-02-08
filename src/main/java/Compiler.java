import antlr.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Path;

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

    MyVisitor visitor = new MyVisitor();
    visitor.visit(tree);
    
    if (parser.getNumberOfSyntaxErrors() > 0) {
      System.out.println("#syntax_error#");
      exit(100);
    }

    System.out.println(tree.toStringTree(parser));
  }

}
