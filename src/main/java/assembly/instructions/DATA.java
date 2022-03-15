package assembly.instructions;

public class DATA extends Instruction {

  public DATA(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return ".data" + label;
  }
}
