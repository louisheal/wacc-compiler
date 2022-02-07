import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;

public class IntLiterTest {

  @Test
  public void numberWithoutSignTokenisedAndParsedCorrectly(){

    String treeResult = "(prog begin (stat (type (baseType int)) x = (assignRHS (expr 5)))"
                      + " end <EOF>)";
    String program = "begin\n"
                   + "int x = 5\n"
                   + "end";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertEquals(tree.toStringTree(parser), treeResult);
  }

  @Test
  public void numberWithNegativeSignTokenisedAndParsedCorrectly(){

    String treeResult = "(prog begin (stat (type (baseType int)) x = "
                      + "(assignRHS (expr -5))) end <EOF>)";
    String program = "begin\n"
                   + "int x = -5\n"
                   + "end";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertEquals(tree.toStringTree(parser), treeResult);
  }

  @Test
  public void numberWithPositiveSignTokenisedAndParsedCorrectly(){

    String treeResult = "(prog begin (stat (type (baseType int)) x = "
                      + "(assignRHS (expr +5))) end <EOF>)";
    String program = "begin\n"
                   + "int x = +5\n"
                   + "end";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertEquals(tree.toStringTree(parser), treeResult);
  }

}
