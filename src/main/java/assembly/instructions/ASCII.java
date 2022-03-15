package assembly.instructions;

public class ASCII extends Instruction {

  //.ascii label
  public ASCII(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return ".ascii " + extraInformation + " " + label;
  }
}
