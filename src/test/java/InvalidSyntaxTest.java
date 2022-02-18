import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class InvalidSyntaxTest {

  SecurityManagerSyntaxCheck securityManager = new SecurityManagerSyntaxCheck();
  int failedTests = 0;
  int totalTests = 0;

  // Will ensure that the files listed are not folders
  FileFilter folderFilter = pathname -> !pathname.isDirectory();

  private void runTests(File[] files) throws IOException {
    for (File file : files) {
      totalTests++;
      System.out.print("RUNNING " + file.getName() + ": ");
      String[] args = {file.toString()};
      try {
        Compiler.main(args);
        System.out.print("FAIL\n");
        failedTests++;
      } catch (SecurityException e) {
        System.out.print("PASS\n");
      }
    }
    System.out.println("--------- Tests passed: " + (totalTests - failedTests) + "/" + totalTests + " ---------\n");
    if (failedTests > 0) {
      Assert.fail();
    }
  }

  @Test
  public void arrayTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/array/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void basicTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/basic/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void expressionsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/expressions/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void functionTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/function/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void ifTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/if/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void pairTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/pairs/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void printTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/print/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void sequenceTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/sequence/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void variablesTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/variables/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void whileTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/invalid/syntaxErr/while/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }


}
