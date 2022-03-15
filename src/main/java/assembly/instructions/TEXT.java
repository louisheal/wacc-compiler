package assembly.instructions;

public class TEXT extends Instruction {

  public TEXT(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return ".text" + label;
  }
}
