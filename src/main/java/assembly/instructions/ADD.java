package assembly.instructions;

import assembly.Flags;
import assembly.Operand2;
import assembly.Register;

public class ADD extends Instruction {

  //ADD dest, operand1, operand2
  public ADD(Register dest, Register operand1, Operand2 operand2) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
  }

  //ADD dest, operand1, operand2, LSL #2
  public ADD(Register dest, Register operand1, Operand2 operand2, long val) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
    this.immValue = val;
  }

  //ADD{S}{cond} dest, operand1, operand2
  public ADD(Register dest, Register operand1, Operand2 operand2, Flags flag) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
    this.extraInformation += flag.toString();
  }

  @Override
  public String toString() {
    if (immValue != null) {
      return String.format("ADD%s %s, %s, %s, LSL #%d", extraInformation, dest, operand1, operand2, immValue);
    }
    return String.format("ADD%s %s, %s, %s", extraInformation, dest, operand1, operand2);
  }
}
