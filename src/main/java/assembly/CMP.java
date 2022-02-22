package assembly;

// Compare
public class CMP extends Instruction {

  Register register;
  private Operand2 op2;
  private int i;

  // This is the case for when the value of a register is compared with an integer
  public CMP(Register register, int i) {
    this.register = register;
    this.i = i;
  }

  // This is the case when the value of a register is compared with a
  public CMP(Register register, Operand2 op2) {
    this.register = register;
    this.op2 = op2;
  }

  @Override
  public String toString() {
    if (op2 == null) { // Case for when register compared with integer
      return "CMP " + register + ", #" + i;
    }
    return "CMP " + register + ", " + op2;
  }


}
