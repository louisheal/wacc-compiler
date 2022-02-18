import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class InvalidSemanticTest {
  SecurityManagerSemanticCheck securityManager = new SecurityManagerSemanticCheck();
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
  public void exitTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\exit\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void expressionsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\expressions\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void functionTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\function\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void ifTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\if\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void IOTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\IO\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void multipleTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\multiple\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void pairsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\pairs\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void printTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\print\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void readTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\read\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void scopeTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\scope\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void variablesTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\variables\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

  @Test
  public void whileTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples\\invalid\\semanticErr\\while\\");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }

}
