package assembly.instructions;

public class LABEL extends Instruction {

  public LABEL(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
