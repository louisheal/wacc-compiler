import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntLiterTest {

  @Test
  public void numberWithoutSignTokenisedAndParsedCorrectly(){

    String treeResult = "(prog begin (stat (type (baseType int)) x = (assignRHS (expr 5)))"
                      + " end <EOF>)";
    String program = "begin\n"
                   + "int x = 5\n"
                   + "end";

    assertEquals(Compiler.lexAnalyse(program), treeResult);
  }

  @Test
  public void numberWithNegativeSignTokenisedAndParsedCorrectly(){

    String treeResult = "(prog begin (stat (type (baseType int)) x = "
                      + "(assignRHS (expr -5))) end <EOF>)";
    String program = "begin\n"
                   + "int x = -5\n"
                   + "end";

    assertEquals(Compiler.lexAnalyse(program), treeResult);
  }

  @Test
  public void numberWithPositiveSignTokenisedAndParsedCorrectly(){

    String treeResult = "(prog begin (stat (type (baseType int)) x = "
                      + "(assignRHS (expr +5))) end <EOF>)";
    String program = "begin\n"
                   + "int x = +5\n"
                   + "end";

    assertEquals(Compiler.lexAnalyse(program), treeResult);
  }

}
