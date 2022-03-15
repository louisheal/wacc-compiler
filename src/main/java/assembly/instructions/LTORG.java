package assembly.instructions;

public class LTORG extends Instruction {

  public LTORG(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return ".ltorg" + label;
  }
}
