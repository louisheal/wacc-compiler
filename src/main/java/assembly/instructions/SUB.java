package assembly.instructions;

import assembly.Flags;
import assembly.Operand2;
import assembly.Register;

public class SUB extends Instruction {

  //SUB{cond} dest, operand1, operand2
  public SUB(Register dest, Register operand1, Operand2 operand2) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
  }

  //SUB{S}{cond} dest, operand1, operand2
  public SUB(Register dest, Register operand1, Operand2 operand2, Flags flag) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
    this.extraInformation += flag.toString();
  }

  public String toString() {
    return "SUB" + extraInformation + " " + dest + ", " + operand1 + ", " + operand2;
  }

}
