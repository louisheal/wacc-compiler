package assembly.instructions;

public class GLOBALMAIN extends Instruction {

  public GLOBALMAIN(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return ".global main" + label;
  }
}
