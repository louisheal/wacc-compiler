package assembly.instructions;

import assembly.Operand2;
import assembly.Register;

public class EOR extends Instruction {

  // EOR rn, rn, #1
  public EOR(Register dest, Register operand1, Operand2 operand2) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
  }

  @Override
  public String toString() {
    return "EOR" + extraInformation + " " + dest + ", " + operand1 + ", " + operand2;
  }
}
