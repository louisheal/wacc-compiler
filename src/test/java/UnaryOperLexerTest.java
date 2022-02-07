import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;

public class UnaryOperLexerTest {

  @Test
  public void unaryOperBangTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper !) "
                      + "(expr x))) end <EOF>)";
    String program = "begin\n"
                   + "print !x\n"
                   + "end";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertEquals(treeResult, tree.toStringTree(parser));
  }

  @Test
  public void unaryOperMinusTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper -) "
                      + "(expr x))) end <EOF>)";
    String program = "begin\n"
                   + "print -x\n"
                   + "end";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertEquals(treeResult, tree.toStringTree(parser));
  }

  @Test
  public void unaryOperLenTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper len) "
                      + "(expr \"compiler\"))) end <EOF>)";
    String program = "begin\n"
                   + "print len \"compiler\"\n"
                   + "end";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertEquals(treeResult, tree.toStringTree(parser));
  }

  @Test
  public void unaryOperOrdTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper ord) "
                      + "(expr 'c'))) end <EOF>)";
    String program = "begin\n"
                   + "print ord 'c'\n"
                   + "end";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertEquals(treeResult, tree.toStringTree(parser));
  }

  @Test
  public void unaryOperChrTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper chr) "
                      + "(expr 99))) end <EOF>)";
    String program = "begin\n"
                   + "print chr 99\n"
                   + "end";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertEquals(treeResult, tree.toStringTree(parser));
  }

}
