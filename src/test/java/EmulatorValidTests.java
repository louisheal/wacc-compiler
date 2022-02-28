import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.junit.Test;

public class EmulatorValidTests {

  MySecurityManager securityManager = new MySecurityManager();

  int failedTests = 0;
  int totalTests = 0;

  // Will ensure that the files listed are not folders
  FileFilter folderFilter = pathname -> !pathname.isDirectory();

  public String scan(File file) throws FileNotFoundException {
    final Scanner scanner = new Scanner(file);
    StringBuilder output = new StringBuilder();
    while (scanner.hasNextLine()) {
      final String lineFromFile = scanner.nextLine();
      if (lineFromFile.contains("Output")) {
        String nextLine = scanner.nextLine();
        // Keeps appending the outputs until there is a gap which is where output stops
        while (!nextLine.equals("")) {
          output.append(nextLine.substring(1).strip()).append("\n");
          nextLine = scanner.nextLine();
        }
      }
    }
    return output.toString();
  }

  @Test
  public void advancedTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/advanced/");
    File[] examples = directory.listFiles();

  }

  @Test
  public void arrayTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/array/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void basicExitTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/basic/exit/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void basicSkipTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/basic/skip/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void expressionsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/expressions/");
    File[] examples = directory.listFiles();

    assert examples != null;
  }

  @Test
  public void nestedFunctionTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/function/nested_functions/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void simpleFunctionTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/function/simple_functions/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void ifTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/if/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void IOTests() throws IOException {
    System.setSecurityManager(securityManager);


    File directory = new File("wacc_examples/valid/IO/");
    File[] examples = directory.listFiles(folderFilter);

    assert examples != null;

  }

  @Test
  public void IOPrintTests() throws IOException {
    System.setSecurityManager(securityManager);


    File directory = new File("wacc_examples/valid/IO/print");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void IOReadTests() throws IOException {
    System.setSecurityManager(securityManager);


    File directory = new File("wacc_examples/valid/IO/read");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void pairsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/pairs/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }



  @Test
  public void runtimeErrorOutOfBoundsTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/runtimeErr/arrayOutOfBounds/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }


  @Test
  public void runtimeErrorDivideByZeroTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/runtimeErr/divideByZero/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }


  @Test
  public void runtimeErrorIntegerOverflowTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/runtimeErr/integerOverflow/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }


  @Test
  public void runtimeErrorNullDereferenceTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/runtimeErr/nullDereference/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void scopeTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/scope/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void sequenceTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/sequence/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void variablesTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/variables/");
    File[] examples = directory.listFiles();

    assert examples != null;

  }

  @Test
  public void whileTests() throws IOException {
    System.setSecurityManager(securityManager);

    File directory = new File("wacc_examples/valid/while/");
    File[] examples = directory.listFiles();

    assert examples != null;
  }
}
