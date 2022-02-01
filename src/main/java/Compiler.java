import antlr.*;
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Compiler {

  public CommonTokenStream tokenise(CharStream input) {
    BasicLexer lexer = new BasicLexer(input);
    return new CommonTokenStream(lexer);
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
