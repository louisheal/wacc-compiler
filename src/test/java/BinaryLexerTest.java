import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;

public class BinaryLexerTest {

  @Test
  public void addition_test1(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = (assignRHS (expr (expr 3) "
        + "(binaryOper +) (expr 5)))) end <EOF>)";
    String program = "begin\n"
        + "int x = 3 + 5\n"
        + "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void addition_test2(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
        "(assignRHS (expr (expr (expr 5) (binaryOper +)" +
        " (expr 4)) (binaryOper +) (expr 2)))) end <EOF>)";
    String program =  "begin\n" +
        "int x = 5 + 4 + 2\n" +
        "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void subtraction_test1(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
                             "(assignRHS (expr (expr 5) (binaryOper -) (expr 2)))) end <EOF>)";
    String program = "begin\n" +
                                "int x = 5 - 2" +
                                "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void subtraction_test2(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
                        "(assignRHS (expr (expr (expr 9) (binaryOper -) (expr 4)) (binaryOper -)" +
                        " (expr 3)))) end <EOF>)";
    String program =  "begin\n" +
                      "int x = 9 - 4 - 3\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void multiplication_test1(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
                                "(assignRHS (expr (expr 2) (binaryOper *) (expr 6)))) end <EOF>)";
    String program = "begin\n" +
                                   "int x = 2 * 6" +
                                   "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void multiplication_test2(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
                        "(assignRHS (expr (expr (expr 5) (binaryOper *)" +
                        " (expr 3)) (binaryOper *) (expr 4)))) end <EOF>)";
    String program =  "begin\n" +
                      "int x = 5 * 3 * 4\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }


  @Test
  public void division_test1(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
                          "(assignRHS (expr (expr 8) (binaryOper /) (expr 4)))) end <EOF>)";
    String program = "begin\n" +
                             "int x = 8 / 4\n" +
                             "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void division_test2(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
                        "(assignRHS (expr (expr (expr 8) (binaryOper /) (expr 4)) (binaryOper /) " +
                        "(expr 2)))) end <EOF>)";
    String program =  "begin\n" +
                      "int x = 8 / 4 / 2\n" +
                      "end";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void modulo_test1(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
                        "(assignRHS (expr (expr 7) (binaryOper %) (expr 3)))) end <EOF>)";
    String program = "begin\n" +
                     "int x = 7 % 3\n" +
                     "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void modulo_test2(){
    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
                        "(assignRHS (expr (expr 200) (binaryOper %) (expr 3)))) end <EOF>)";
    String program =  "begin\n" +
                      "int x = 200 % 3\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void greater_than_test1(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 9) (binaryOper >) (expr 8)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 9 > 8\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void greater_than_test2(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 1) (binaryOper >) (expr 2)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 1 > 2\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void greater_than_or_equal_test1(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 5) (binaryOper >=) (expr 5)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 5 >= 5\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void greater_than_or_equal_test2(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 4) (binaryOper >=) (expr 5)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 4 >= 5\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void less_than_test1(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 6) (binaryOper <) (expr 9)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 6 < 9\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void less_than_test2(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 10) (binaryOper <) (expr 9)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 10 < 9\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void less_than_or_equal_test1(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
        "(assignRHS (expr (expr 3) (binaryOper <=) (expr 4)))) end <EOF>)";
    String program =  "begin\n" +
        "bool x = 3 <= 4\n" +
        "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void less_than_or_equal_test2(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 9) (binaryOper <=) (expr 4)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 9 <= 4\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void equal_test1(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 4) (binaryOper ==) (expr 4)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 4 == 4\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void equal_test2(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 2) (binaryOper ==) (expr 1)))) end <EOF>)";
    String program =  "begin\n" +
        "bool x = 2 == 1\n" +
        "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void not_equal_test1(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 1) (binaryOper !=) (expr 2)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 1 != 2\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }

  @Test
  public void not_equal_test2(){
    String treeResult = "(prog begin (stat (type (baseType bool)) x = " +
                        "(assignRHS (expr (expr 2) (binaryOper !=) (expr 2)))) end <EOF>)";
    String program =  "begin\n" +
                      "bool x = 2 != 2\n" +
                      "end\n";
    CharStream input = CharStreams.fromString(program);
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals(treeResult));
  }



}