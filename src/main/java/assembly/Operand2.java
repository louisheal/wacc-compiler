package assembly;

public class Operand2 {

  private Register register;
  private Shift shift;
  private int amount;
  private int immediateValue;
  private int offset;
  private boolean isOffsetRegister;


  // Register with no shift
  public Operand2(Register register) {
    this.register = register;
    this.isOffsetRegister = false;
  }

  // Register with (LSL/LSR) shift with amount
  public Operand2(Register register, Shift shift, int amount) {
    this.register = register;
    this.shift = shift;
    this.amount = amount;
    this.isOffsetRegister = false;
  }

  //Register with offset
  public Operand2(Register register, int offset) {
    this.register = register;
    this.offset = offset;
    this.isOffsetRegister = true;
  }

  // Immediate value
  public Operand2(int immediateValue) {
    this.immediateValue = immediateValue;
    register = null;
    shift = null;
    this.isOffsetRegister = false;
  }

  @Override
  public String toString() {

    if (shift == null && register == null) { // the operand is an immediate value
      return "#" + immediateValue;
    } else if (shift != null && register != null) { // the operand is a register shift
      return register + ", " + shift + ", #" + amount;
    } else if (isOffsetRegister) {
      return register + ", #" + offset;
    }
    return register.toString();
  }

}
