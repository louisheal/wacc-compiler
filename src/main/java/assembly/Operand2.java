package assembly;

public class Operand2 {

  private Register register;
  private Shift shift;
  private int amount;
  private int immediateValue;

  // Register with no shift
  public Operand2(Register register) {
    this.register = register;
  }

  // Register with (LSL/LSR) shift with amount
  public Operand2(Register register, Shift shift, int amount) {
    this.register = register;
    this.shift = shift;
    this.amount = amount;
  }

  // Immediate value
  public Operand2(int immediateValue) {
    this.immediateValue = immediateValue;
    register = null;
    shift = null;
  }

  @Override
  public String toString() {

    if (shift == null && register == null) { // the operand is an immediate value
      return "#" + immediateValue + "\n";
    } else if (shift != null && register != null) { // the operand is a register shift
      return "%" + register + ", %" + shift + ", #" + amount + "\n";
    }

    return "%" + register + "\n";
  }

}
