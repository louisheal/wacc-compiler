import static org.junit.Assert.assertTrue;

import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;

public class BinaryLexerTest {

  @Test
  public void addition_test(){
    CharStream input = CharStreams.fromString("3+5");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 3)) (binaryOper +) "
        + "(expr (intLiter 5))) <EOF>)"));
  }

  @Test
  public void subtraction_test(){
    CharStream input = CharStreams.fromString("9-3");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 9)) (binaryOper -) "
        + "(expr (intLiter 3))) <EOF>)"));
  }

  @Test
  public void multiplication_test(){
    CharStream input = CharStreams.fromString("2*6");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 2)) (binaryOper *) "
        + "(expr (intLiter 6))) <EOF>)"));
  }

  @Test
  public void division_test(){
    CharStream input = CharStreams.fromString("8/4");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 8)) (binaryOper /) "
        + "(expr (intLiter 4))) <EOF>)"));
  }

  @Test
  public void modulo_test(){
    CharStream input = CharStreams.fromString("7%3");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 7)) (binaryOper %) "
        + "(expr (intLiter 3))) <EOF>)"));
  }

  @Test
  public void greater_than_test(){
    CharStream input = CharStreams.fromString("9>8");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 9)) (binaryOper >) "
        + "(expr (intLiter 8))) <EOF>)"));
  }

  @Test
  public void greater_than_or_equal_test(){
    CharStream input = CharStreams.fromString("5>=5");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 5)) (binaryOper >=) "
        + "(expr (intLiter 5))) <EOF>)"));
  }

  @Test
  public void less_than_test(){
    CharStream input = CharStreams.fromString("6<9");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 6)) (binaryOper <) "
        + "(expr (intLiter 9))) <EOF>)"));
  }

  @Test
  public void less_than_or_equal_test(){
    CharStream input = CharStreams.fromString("3<=4");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 3)) (binaryOper <=) "
        + "(expr (intLiter 4))) <EOF>)"));
  }

  @Test
  public void equal_test(){
    CharStream input = CharStreams.fromString("4==4");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 4)) (binaryOper ==) "
        + "(expr (intLiter 4))) <EOF>)"));
  }

  @Test
  public void not_equal_test(){
    CharStream input = CharStreams.fromString("1!=2");
    BasicLexer lexer = new BasicLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    assertTrue(tree.toStringTree(parser).equals("(prog (expr (expr (intLiter 1)) (binaryOper !=) "
        + "(expr (intLiter 2))) <EOF>)"));
  }



}