import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnaryOperLexerTest {

  @Test
  public void unaryOperBangTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper !) "
                      + "(expr x))) end <EOF>)";
    String program = "begin\n"
                   + "print !x\n"
                   + "end";

    assertEquals(Compiler.lexAnalyse(program), treeResult);
  }

  @Test
  public void unaryOperMinusTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper -) "
                      + "(expr x))) end <EOF>)";
    String program = "begin\n"
                   + "print -x\n"
                   + "end";

    assertEquals(Compiler.lexAnalyse(program), treeResult);
  }

  @Test
  public void unaryOperLenTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper len) "
                      + "(expr \"compiler\"))) end <EOF>)";
    String program = "begin\n"
                   + "print len \"compiler\"\n"
                   + "end";

    assertEquals(Compiler.lexAnalyse(program), treeResult);
  }

  @Test
  public void unaryOperOrdTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper ord) "
                      + "(expr 'c'))) end <EOF>)";
    String program = "begin\n"
                   + "print ord 'c'\n"
                   + "end";

    assertEquals(Compiler.lexAnalyse(program), treeResult);
  }

  @Test
  public void unaryOperChrTokenizesCorrectly(){

    String treeResult = "(prog begin (stat print (expr (unaryOper chr) "
                      + "(expr 99))) end <EOF>)";
    String program = "begin\n"
                   + "print chr 99\n"
                   + "end";

    assertEquals(Compiler.lexAnalyse(program), treeResult);
  }

}
