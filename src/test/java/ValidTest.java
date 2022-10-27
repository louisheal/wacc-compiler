import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class ValidTest {

  MySecurityManager securityManager = new MySecurityManager();

  int failedTests = 0;
  int totalTests = 0;

  public void runTests(File[] files) throws IOException {
    for (File file : files) {
      totalTests++;
      System.out.print("RUNNING " + file.getName() + ": ");
      try {
        Compiler.compile(file.toString());
        System.out.print("PASS\n");
      } catch (Exception e) {
        System.out.print("FAIL\n");
        failedTests++;
      }
    }
    System.out.println("--------- Tests passed: " + (totalTests - failedTests) + "/" + totalTests
        + " ---------\n");
    if (failedTests > 0) {
      Assert.fail();
    }
  }

  // Will ensure that the files listed are not folders
  FileFilter folderFilter = pathname -> !pathname.isDirectory();

  @Test
  public void advancedTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/advanced/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void arrayTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/array/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void basicExitTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/basic/exit/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void basicSkipTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/basic/skip/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void expressionsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/expressions/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void nestedFunctionTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/function/nested_functions/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void simpleFunctionTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/function/simple_functions/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void ifTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/if/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void IOTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/IO/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void IOPrintTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/IO/print");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void IOReadTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/IO/read");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void pairsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/pairs/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }


  //TODO: NEED TO DO DIFFERENT EXIT CALL CHECKS FOR RUNTIME ERRORS
  @Test
  public void runtimeErrorOutOfBoundsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/runtimeErr/arrayOutOfBounds/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  //TODO: NEED TO DO DIFFERENT EXIT CALL CHECKS FOR RUNTIME ERRORS
  @Test
  public void runtimeErrorDivideByZeroTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/runtimeErr/divideByZero/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  //TODO: NEED TO DO DIFFERENT EXIT CALL CHECKS FOR RUNTIME ERRORS
  @Test
  public void runtimeErrorIntegerOverflowTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/runtimeErr/integerOverflow/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  //TODO: NEED TO DO DIFFERENT EXIT CALL CHECKS FOR RUNTIME ERRORS
  @Test
  public void runtimeErrorNullDereferenceTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/runtimeErr/nullDereference/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void scopeTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/scope/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void sequenceTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/sequence/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void variablesTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/variables/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void whileTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/while/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void functionOverloadingTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/extension/functionOverloading/");
    File[] examples = directory.listFiles();

    assert examples != null;
    runTests(examples);
  }

}
