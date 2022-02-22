package assembly;

public class ADD extends Instruction {
  //TODO: add flags that can be used in ADD instruction
  private Register op1;
  private Operand2 op2;
  private int value;

  // Adding the values in Register op1 and Operand2
  public ADD(Register dest, Register op1, Operand2 op2) {
    this.dest = dest;
    this.op1 = op1;
    this.op2 = op2;
  }

  // Adding the values in Register op1 and the int value
  public ADD(Register dest, Register op1, int value) {
    this.dest = dest;
    this.op1 = op1;
    this.value = value;
  }

  @Override
  public String toString() {
    if (op2 == null) {
      return "ADD " + dest + ", " + op1 + ", "  + value;
    }
    return "ADD " + dest + ", " + op1 + ", " + op2;
  }
}
