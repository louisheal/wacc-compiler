package assembly.instructions;

import assembly.Flags;
import assembly.Operand2;
import assembly.Register;

public class ADD extends Instruction {

  //ADD{cond} dest, operand1, operand2
  public ADD(Register dest, Register operand1, Operand2 operand2) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
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
    return "ADD" + extraInformation + " " + dest + ", " + operand1 + ", " + operand2;
  }
}
