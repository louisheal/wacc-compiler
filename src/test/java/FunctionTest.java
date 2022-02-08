import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FunctionTest {

  @Test
  public void basicFunctionTokenizesAndParsesCorrectly() {

    String treeResult = "(prog begin (func (type (baseType int)) "
            + "foo ( ) is (stat skip) end) (stat skip) end <EOF>)";
    String program = "begin\n"
            + "int foo ( ) is skip end\n"
            + "skip\n"
            + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void functionWithParamsTokenizesAndParsesCorrectly() {

    String treeResult = "(prog begin (func (type (baseType int)) "
            + "foo ( (paramList (param (type (baseType int)) x) , "
            + "(param (type (baseType int)) y)) ) is (stat skip) end) "
            + "(stat skip) end <EOF>)";
    String program = "begin\n"
            + "int foo (int x, int y) is skip end\n"
            + "skip\n"
            + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void mutuallyRecursiveFunctionsTokenizeAndParseCorrectly() {

    String treeResult = "(prog begin "
            + "(func (type (baseType int)) foo ( ) is (stat (type (baseType int)) y "
            + "= (assignRHS call bar ( argList ))) end) "
            + "(func (type (baseType int)) bar ( ) is (stat (type (baseType int)) x "
            + "= (assignRHS call foo ( argList ))) end) "
            + "(stat skip) end <EOF>)";
    String program = "begin\n"
            + "int foo ( ) is int y = call bar() end\n"
            + "int bar ( ) is int x = call foo() end\n"
            + "skip\n"
            + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

}
