import static org.junit.Assert.assertTrue;

import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;

public class StatTest {

  String tree1 = "(prog begin (stat skip) end <EOF>)";
  String program1 = "begin"
                  + "skip"
                  + "end";

  String tree2 = "(prog begin (stat (type (baseType int)) x = (assignRHS (expr 5))) end <EOF>)";
  String program2 = "begin"
                  + "int x = 5"
                  + "end";

  String tree3 = "(prog begin (stat (assignLHS x) = (assignRHS (expr 5))) end <EOF>)";
  String program3 = "begin"
                  + "x = 5"
                  + "end";

  @Test
  public void skipProgramTokenizesAndParsesCorrectly(){
    CharStream input = CharStreams.fromString(program1);

    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertTrue(tree.toStringTree(parser).equals(tree1));
  }

  @Test
  public void typeIdentProgramTokenizesAndParsesCorrectly(){
    CharStream input = CharStreams.fromString(program2);

    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertTrue(tree.toStringTree(parser).equals(tree2));
  }

  @Test
  public void assignLHSRHSProgramTokenizesAndParsesCorrectly(){
    CharStream input = CharStreams.fromString(program3);

    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    assertTrue(tree.toStringTree(parser).equals(tree3));
  }

}
