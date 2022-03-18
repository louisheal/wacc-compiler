import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class ExtensionTests {
  MySecurityManager securityManager = new MySecurityManager();

  int failedTests = 0;
  int totalTests = 0;

  // Will ensure that the files listed are not folders
  FileFilter folderFilter = pathname -> !pathname.isDirectory();

  public void runTests(File[] files) {
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
    System.out.println("--------- Tests passed: " + (totalTests - failedTests) + "/" + totalTests + " ---------\n");
    if (failedTests > 0) {
      Assert.fail();
    }
  }

  @Test
  public void functionOverloadingTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/extension/functionOverloading/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;
    runTests(examples);
  }


}
