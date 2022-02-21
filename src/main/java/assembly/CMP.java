package assembly;

public class CMP {

  Register register;
  private Operand op2;
  private int i;

  // This is the case for when the value of a register is compared with an integer
  public CMP(Register register, int i) {
    this.register = register;
    this.i = i;
  }

  //TODO: Create the case when a register is compared with another operand (op2)

  @Override
  public String toString() {
    return "CMP %" + register + ", %" + i + "\n";
  }


}
