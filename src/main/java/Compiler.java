import antlr.*;
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Compiler {

  public static CommonTokenStream tokenise(CharStream input) {
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
    return parse(tokenise(input));
  }

  public static void main(String[] args) throws IOException {

    CharStream input = CharStreams.fromStream(System.in);

    BasicLexer lexer = new BasicLexer(input);

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    BasicParser parser = new BasicParser(tokens);

    ParseTree tree = parser.prog();

    System.out.println(tree.toStringTree(parser));

  }

}
