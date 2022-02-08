import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinOpPrecedenceTest {

  @Test
  public void addThenMultiplyHasCorrectOrdering() {

    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
            "(assignRHS (expr (expr (intLiter 5)) (binaryOper +) " +
            "(expr (expr (intLiter 3)) (binaryOper *) (expr (intLiter 9)))" +
            "))) end <EOF>)";
    String program = "begin\n"
            + "int x = 5 + 3 * 9\n"
            + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void subtractThenMultiplyHasCorrectOrdering() {

    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
            "(assignRHS (expr (expr (intLiter 5)) (binaryOper -) " +
            "(expr (expr (intLiter 3)) (binaryOper *) (expr (intLiter 9)))" +
            "))) end <EOF>)";
    String program = "begin\n"
            + "int x = 5 - 3 * 9\n"
            + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void addThenDivideHasCorrectOrdering() {

    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
            "(assignRHS (expr (expr (intLiter 5)) (binaryOper +) " +
            "(expr (expr (intLiter 9)) (binaryOper /) (expr (intLiter 3)))" +
            "))) end <EOF>)";
    String program = "begin\n"
            + "int x = 5 + 9 / 3\n"
            + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void subtractThenDivideHasCorrectOrdering() {

    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
            "(assignRHS (expr (expr (intLiter 5)) (binaryOper -) " +
            "(expr (expr (intLiter 9)) (binaryOper /) (expr (intLiter 3)))" +
            "))) end <EOF>)";
    String program = "begin\n"
            + "int x = 5 - 9 / 3\n"
            + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }
}
