import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StatTest {

  @Test
  public void skipStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat skip) end <EOF>)";
    String program = "begin\n"
                   + "skip\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void typeIdentStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat (type (baseType int)) x = " +
        "(assignRHS (expr (intLiter 5)))) end <EOF>)";
    String program = "begin\n"
                   + "int x = 5\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void assignLHSRHSStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat (assignLHS x) = " +
        "(assignRHS (expr (intLiter 5)))) end <EOF>)";
    String program = "begin\n"
                   + "x = 5\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void readStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat read (assignLHS x)) end <EOF>)";
    String program = "begin\n"
                   + "read x\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void freeStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat free (expr x)) end <EOF>)";
    String program = "begin\n"
                   + "free x\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void returnStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat return (expr x)) end <EOF>)";
    String program = "begin\n"
                   + "return x\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void exitStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat exit (expr x)) end <EOF>)";
    String program = "begin\n"
                   + "exit x\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void printStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat print (expr x)) end <EOF>)";
    String program = "begin\n"
                   + "print x\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void printlnStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat println (expr x)) end <EOF>)";
    String program = "begin\n"
                   + "println x\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void ifStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat if (expr x) then "
                      + "(stat print (expr y)) else "
                      + "(stat print (expr z)) fi) end <EOF>)";
    String program = "begin\n"
            + "if x then print y else print z fi\n"
            + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void whileStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat while (expr x) do "
                      + "(stat print (expr y)) done) end <EOF>)";
    String program = "begin\n"
                   + "while x do print y done\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void beginStatementTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat begin (stat print (expr x)) end) end <EOF>)";
    String program = "begin\n"
                   + "begin print x end\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

  @Test
  public void statementRecursionTokenizesAndParsesCorrectly(){

    String treeResult = "(prog begin (stat (stat print (expr x)) ; "
                      + "(stat print (expr y))) end <EOF>)";
    String program = "begin\n"
                   + "print x;\n"
                   + "print y\n"
                   + "end";

    assertEquals(treeResult, Compiler.lexAnalyse(program));
  }

}
