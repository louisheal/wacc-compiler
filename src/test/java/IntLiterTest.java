import static org.junit.Assert.assertTrue;

import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;

public class IntLiterTest {

  @Test
  public void numberWithoutSignTokenisedAndParsedCorrectly(){
    String program = "begin\n" +
        "int x = 5\n" +
        "end\n";
    String treeResult = "(prog begin (stat (type (baseType int)) x = (assignRHS (expr 5)))" +
                        " end <EOF>)";

    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    System.out.println(tree.toStringTree(parser));
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void numberWithNegativeSignTokenisedAndParsedCorrectly(){
    String program = "begin\n" +
        "int x = -5\n" +
        "end\n";
    String treeResult = "(prog begin (stat (type (baseType int)) x = (assignRHS (expr -5))) " +
                        "end <EOF>)";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
//    assertTrue(tree.toStringTree(parser).equals("(prog (expr (intLiter (intSign -) 5)) <EOF>)"));
  }

  @Test
  public void numberWithPositiveSignTokenisedAndParsedCorrectly(){
    String program = "begin\n" +
        "int x = +5\n" +
        "end\n";
    String treeResult = "(prog begin (stat (type (baseType int)) x = (assignRHS (expr +5))) " +
                        "end <EOF>)";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
//    assertTrue(tree.toStringTree(parser).equals("(prog (expr (intLiter (intSign +) 5)) <EOF>)"));

  }

}
