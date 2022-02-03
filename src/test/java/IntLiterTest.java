import static org.junit.Assert.assertTrue;

import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;

public class IntLiterTest {

  @Test
  public void numberWithoutSignTokenisedAndParsedCorrectly(){
    CharStream input = CharStreams.fromString("5");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (intLiter 5)) <EOF>)"));
  }

  @Test
  public void numberWithNegativeSignTokenisedAndParsedCorrectly(){
    CharStream input = CharStreams.fromString("-5");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (intLiter (intSign -) 5)) <EOF>)"));
  }

  @Test
  public void numberWithPositiveSignTokenisedAndParsedCorrectly(){
    CharStream input = CharStreams.fromString("+5");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    System.out.println(tree.toStringTree(parser));
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (intLiter (intSign +) 5)) <EOF>)"));

  }

}
